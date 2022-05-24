package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Adding
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Moving
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Removing
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Teleporting
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Updating
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.buffer.writeBytes
import java.util.Optional
import kotlin.math.abs

/**
 * @author Jordan Abraham
 */
onPacketAssembler<PlayerInfoPacket>(opcode = 80, size = -2) {
    buildPacket {
        val blocks = BytePacketBuilder()
        viewport.resize()
        repeat(2) { highDefinition(viewport, blocks, highDefinitionUpdates, steps, it == 0) }
        repeat(2) { lowDefinition(viewport, blocks, lowDefinitionUpdates, players, it == 0) }
        viewport.update()
        writePacket(blocks.build())
    }
}

fun BytePacketBuilder.highDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    highDefinitionUpdates: Map<Player, Optional<ByteArray>>,
    steps: Map<Player, Optional<MovementStep>>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    for (it in 0 until viewport.highDefinitionsCount) {
        val index = viewport.highDefinitions[it]
        if (viewport.isNsn(index) == nsn) continue
        val other = viewport.players[index]
        val updates = highDefinitionUpdates[other]
        val movementStep = steps[other]
        // Check the activities this player is doing.
        val activity = highDefinitionActivities(viewport, other, updates, movementStep)
        if (other == null || activity == null || blocks.size >= Short.MAX_VALUE) {
            viewport.setNsn(index)
            skip++
            continue
        }
        // Write player skips.
        skip = skipPlayers(skip)
        // This player has an activity update (true).
        writeBit { true }
        // Write corresponding bits depending on the activity type the player is doing.
        activity.writeBits(this@withBitAccess, viewport, index, updates?.isPresent == true, other.location, other.previousLocation ?: other.location, movementStep)
        if (activity is Removing) {
            viewport.players[index] = null
        } else {
            if (activity !is Updating) {
                // Update server with new location if this player moved.
                viewport.locations[index] = other.location.regionLocation
            }
            if (updates?.isPresent == true) {
                // Since we hard check if the player has a blocks update, write the buffer here.
                blocks.writeBytes(updates::get)
            }
        }
    }
    skipPlayers(skip)
}

fun BytePacketBuilder.lowDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    lowDefinitionUpdates: Map<Player, Optional<ByteArray>>,
    players: Map<Int, Player>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    for (it in 0 until viewport.lowDefinitionsCount) {
        val index = viewport.lowDefinitions[it]
        if (!viewport.isNsn(index) == nsn) continue
        val other = players[index]
        // Check the activities this player is doing.
        val activity = lowDefinitionActivities(viewport, other)
        if (other == null || activity == null || blocks.size >= Short.MAX_VALUE) {
            viewport.setNsn(index)
            skip++
            continue
        }
        val updates = lowDefinitionUpdates[other]
        if (updates == null) {
            viewport.setNsn(index)
            skip++
            continue
        }
        // Write player skips.
        skip = skipPlayers(skip)
        // This player has an activity update (true).
        writeBit { true }
        // Write corresponding bits depending on the activity type the player is doing.
        activity.writeBits(this@withBitAccess, viewport, index, current = other.location, previous = other.previousLocation ?: other.location)
        if (activity is Adding) {
            blocks.writeBytes(updates::get)
            // Add them to our array.
            viewport.players[index] = other
            viewport.setNsn(index)
        }
    }
    skipPlayers(skip)
}

fun BitAccess.skipPlayers(count: Int): Int {
    // Check if there are any players to skip.
    if (count == -1) return count
    // This player has no activity update (false).
    writeBit { false }
    when (count) {
        0 -> writeBits(2) { 0 }
        in 1 until 32 -> {
            writeBits(2) { 1 }
            writeBits(5) { count }
        }
        in 32 until 256 -> {
            writeBits(2) { 2 }
            writeBits(8) { count }
        }
        in 256 until 2048 -> {
            writeBits(2) { 3 }
            writeBits(11) { count }
        }
        else -> throw IllegalArgumentException("Skip count is not within range of 0-2047.")
    }
    return -1
}

fun highDefinitionActivities(
    viewport: Viewport,
    other: Player?,
    highDefinitionUpdate: Optional<ByteArray>?,
    movementStep: Optional<MovementStep>?
): ActivityUpdateType? {
    val ourLocation = viewport.player.location
    val theirLocation = other?.location
    return when {
        // If the player needs to be removed from high definition to low definition.
        other?.online == false || theirLocation == null || !theirLocation.withinDistance(ourLocation, viewport.viewDistance) -> Removing
        // If the player is moving (Teleporting, Walking, Running).
        movementStep?.isPresent == true -> if (movementStep.get().speed == MovementSpeed.TELEPORTING) Teleporting else Moving
        // If the player has block updates.
        highDefinitionUpdate?.isPresent == true -> Updating
        else -> null
    }
}

fun lowDefinitionActivities(
    viewport: Viewport,
    other: Player?
): ActivityUpdateType? {
    val ourLocation = viewport.player.location
    val theirLocation = other?.location
    return when {
        // If the player needs to be added from low definition to high definition.
        theirLocation != null && theirLocation.withinDistance(ourLocation, viewport.viewDistance) -> Adding
        else -> null
    }
}

sealed class ActivityUpdateType {
    object Removing : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: Optional<MovementStep>?) {
            // Player has no update.
            bits.writeBit { false }
            // The player is not moving.
            bits.writeBits(2) { 0 }
            bits.updateLocation(viewport, index, current)
        }
    }

    object Teleporting : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: Optional<MovementStep>?) {
            // If the player has pending block updates.
            bits.writeBit { updating }
            // Make the player teleport.
            bits.writeBits(2) { 3 }
            var deltaX = current.x - previous.x
            var deltaZ = current.z - previous.z
            val deltaLevel = current.level - previous.level
            if (abs(current.x - previous.x) <= 14 && abs(current.z - previous.z) <= 14) {
                bits.writeBit { false }
                if (deltaX < 0) deltaX += 32
                if (deltaZ < 0) deltaZ += 32
                bits.writeBits(12) { deltaZ or (deltaX shl 5) or (deltaLevel shl 10) }
            } else {
                bits.writeBit { true }
                bits.writeBits(30) { (deltaZ and 0x3fff) or (deltaX and 0x3fff shl 14) or (deltaLevel and 0x3 shl 28) }
            }
        }
    }

    object Moving : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: Optional<MovementStep>?) {
            val running = step!!.get().speed.isRunning()
            // If the player has pending block updates.
            bits.writeBit { updating }
            // Make the player walk or run.
            bits.writeBits(2) { if (running) 2 else 1 }
            bits.writeBits(if (running) 4 else 3) { step.get().direction.playerOpcode(running) }
        }
    }

    object Updating : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: Optional<MovementStep>?) {
            // The player has pending block updates.
            bits.writeBit { true }
            // The player is not moving.
            bits.writeBits(2) { 0 }
        }
    }

    object Adding : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: Optional<MovementStep>?) {
            bits.writeBits(2) { 0 }
            // Update the player location.
            bits.updateLocation(viewport, index, current)
            bits.writeBits(13, current::x)
            bits.writeBits(13, current::z)
            // Update the player blocks.
            bits.writeBit { true }
        }
    }

    abstract fun writeBits(
        bits: BitAccess,
        viewport: Viewport,
        index: Int,
        updating: Boolean = false,
        current: Location,
        previous: Location,
        step: Optional<MovementStep>? = null
    )

    fun BitAccess.updateLocation(viewport: Viewport, index: Int, location: Location) {
        val current = viewport.locations[index]
        val next = location.regionLocation
        if (next == current) writeBit { false }
        else {
            // Write the new location.
            writeLocation(current, next)
            // Update server with new location.
            viewport.locations[index] = next
        }
    }

    private fun BitAccess.writeLocation(previous: Int, current: Int) {
        // Write there is a location change.
        writeBit { true }
        val previousLevel = previous shr 16
        val previousX = previous shr 8
        val previousZ = previous and 0xff
        val currentLevel = current shr 16
        val currentX = current shr 8
        val currentZ = current and 0xff
        val deltaLevel = currentLevel - previousLevel
        val deltaX = currentX - previousX
        val deltaZ = currentZ - previousZ
        when {
            previousX == currentX && previousZ == currentZ -> {
                writeBits(2) { 1 }
                writeBits(2) { deltaLevel }
            }
            abs(currentX - previousX) <= 1 && abs(currentZ - previousZ) <= 1 -> {
                writeBits(2) { 2 }
                writeBits(5) { (deltaLevel shl 3) or (Direction.directionFromDelta(deltaX, deltaZ).playerOpcode() and 0x7) }
            }
            else -> {
                writeBits(2) { 3 }
                writeBits(18) { (deltaZ and 0xff) or (deltaX and 0xff shl 8) or (deltaLevel shl 16) }
            }
        }
    }
}

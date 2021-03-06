package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeFully
import org.jctools.maps.NonBlockingHashMapLong
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Adding
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Moving
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Removing
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Teleporting
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Updating
import script.packet.assembler.PlayerInfoAssembler.RegionLocationChange.FullLocationChange
import script.packet.assembler.PlayerInfoAssembler.RegionLocationChange.LevelLocationChange
import script.packet.assembler.PlayerInfoAssembler.RegionLocationChange.PartialLocationChange
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.game.world.map.Location
import xlitekt.game.world.map.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.dynamicBuffer
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.insert
import java.nio.ByteBuffer
import kotlin.math.abs

/**
 * @author Jordan Abraham
 */
private val blockBufferLimit = 15000

insert<PacketAssemblerListener>().assemblePacket<PlayerInfoPacket>(opcode = 80, size = -2) {
    viewport.resize()
    it.writeBytes(
        dynamicBuffer {
            repeat(2) { nsn -> it.highDefinition(viewport, this, highDefinitionUpdates, movementStepsUpdates, alternativeHighDefinitionUpdates, nsn == 0) }
            repeat(2) { nsn -> it.lowDefinition(viewport, this, lowDefinitionUpdates, players, alternativeLowDefinitionUpdates, nsn == 0) }
        }
    )
    viewport.update()
}

fun ByteBuffer.highDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    highDefinitionUpdates: Array<ByteArray?>,
    movementStepsUpdates: Array<MovementStep?>,
    alternativeHighDefinitionUpdates: Array<ByteArray?>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    for (it in 0 until viewport.highDefinitionsCount) {
        val index = viewport.highDefinitions[it]
        if (viewport.isNsn(index) == nsn) continue
        val other = viewport.players[index]
        val updates = other?.let { highDefinitionUpdates[other.index] }
        val movementStep = other?.let { movementStepsUpdates[other.index] }
        // Check the activities this player is doing.
        val activity = viewport.highDefinitionActivities(other, updates, movementStep, blocks.size + ((bitIndex + 7) / 8))
        if (other == null || activity == null) {
            viewport.setNsn(index)
            skip++
            continue
        }
        // Write player skips.
        skip = skipPlayers(skip)
        // This player has an activity update (true).
        writeBit(true)
        // Write corresponding bits depending on the activity type the player is doing.
        activity.writeBits(this@withBitAccess, viewport, index, updates != null, other.location, other.previousLocation, movementStep)
        if (activity is Removing) {
            viewport.players[index] = null
        } else {
            if (activity !is Updating) {
                // Update server with new location if this player moved.
                viewport.locations[index] = other.location.regionLocation
            }
            if (updates != null) {
                blocks.writeFully(alternativeHighDefinitionUpdates[other.index] ?: updates)
            }
        }
    }
    skipPlayers(skip)
}

fun ByteBuffer.lowDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    lowDefinitionUpdates: Array<ByteArray?>,
    players: NonBlockingHashMapLong<Player>,
    alternativeLowDefinitionUpdates: Array<ByteArray?>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    for (it in 0 until viewport.lowDefinitionsCount) {
        val index = viewport.lowDefinitions[it]
        if (!viewport.isNsn(index) == nsn) continue
        val other = players[index.toLong()]
        val updates = other?.let { lowDefinitionUpdates[other.index] }
        // Check the activities this player is doing.
        val activity = viewport.lowDefinitionActivities(other, updates, blocks.size + ((bitIndex + 7) / 8))
        if (other == null || activity == null) {
            viewport.setNsn(index)
            skip++
            continue
        }
        // Write player skips.
        skip = skipPlayers(skip)
        // This player has an activity update (true).
        writeBit(true)
        // Write corresponding bits depending on the activity type the player is doing.
        activity.writeBits(this@withBitAccess, viewport, index, current = other.location, previous = other.previousLocation, step = null)
        if (activity is Adding) {
            blocks.writeFully(alternativeLowDefinitionUpdates[other.index] ?: updates!!)
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
    writeBit(false)
    when (count) {
        0 -> writeBits(2, 0)
        in 1 until 32 -> {
            writeBits(2, 1)
            writeBits(5, count)
        }
        in 32 until 256 -> {
            writeBits(2, 2)
            writeBits(8, count)
        }
        in 256 until 2048 -> {
            writeBits(2, 3)
            writeBits(11, count)
        }
        else -> throw IllegalArgumentException("Skip count is not within range of 0-2047.")
    }
    return -1
}

fun Viewport.highDefinitionActivities(other: Player?, updates: ByteArray?, movementStep: MovementStep?, size: Int): ActivityUpdateType? {
    val ourLocation = player.location
    val theirLocation = other?.location ?: Location.None
    return when {
        // If the player needs to be removed from high definition to low definition.
        other?.online == false || theirLocation == Location.None || !theirLocation.withinDistance(ourLocation, viewDistance) -> Removing
        // If the player is moving (Teleporting, Walking, Running).
        movementStep != null -> if (movementStep.speed == MovementSpeed.Teleporting) Teleporting else Moving
        // If the player has block updates.
        size + (updates?.size ?: 0) <= blockBufferLimit && updates != null -> Updating
        else -> null
    }
}

fun Viewport.lowDefinitionActivities(other: Player?, updates: ByteArray?, size: Int): ActivityUpdateType? {
    val ourLocation = player.location
    val theirLocation = other?.location ?: Location.None
    return when {
        // If the player needs to be added from low definition to high definition.
        size + (updates?.size ?: 0) <= blockBufferLimit && other?.online == true && theirLocation != Location.None && theirLocation.withinDistance(ourLocation, viewDistance) -> Adding
        updates == null -> null
        else -> null
    }
}

sealed class ActivityUpdateType {
    object Removing : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            // Player has no update.
            bits.writeBit(false)
            // The player is not moving.
            bits.writeBits(2, 0)
            bits.updateLocation(viewport, index, current)
        }
    }

    object Teleporting : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            // If the player has pending block updates.
            bits.writeBit(updating)
            // Make the player teleport.
            bits.writeBits(2, 3)
            var deltaX = current.x - previous.x
            var deltaZ = current.z - previous.z
            val deltaLevel = current.level - previous.level
            if (abs(current.x - previous.x) <= 14 && abs(current.z - previous.z) <= 14) {
                bits.writeBit(false)
                if (deltaX < 0) deltaX += 32
                if (deltaZ < 0) deltaZ += 32
                bits.writeBits(12, deltaZ or (deltaX shl 5) or (deltaLevel shl 10))
            } else {
                bits.writeBit(true)
                bits.writeBits(30, (deltaZ and 0x3fff) or (deltaX and 0x3fff shl 14) or (deltaLevel and 0x3 shl 28))
            }
        }
    }

    object Moving : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            val running = step?.speed?.running ?: false
            // If the player has pending block updates.
            bits.writeBit(updating)
            // Make the player walk or run.
            bits.writeBits(2, if (running) 2 else 1)
            val opcode = step?.direction?.opcodeForPlayerDirection ?: 0
            bits.writeBits(if (running) 4 else 3, opcode)
        }
    }

    object Updating : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            // The player has pending block updates.
            bits.writeBit(true)
            // The player is not moving.
            bits.writeBits(2, 0)
        }
    }

    object Adding : ActivityUpdateType() {
        override fun writeBits(bits: BitAccess, viewport: Viewport, index: Int, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            bits.writeBits(2, 0)
            // Update the player location.
            bits.updateLocation(viewport, index, current)
            bits.writeBits(13, current.x)
            bits.writeBits(13, current.z)
            // Update the player blocks.
            bits.writeBit(true)
        }
    }

    abstract fun writeBits(
        bits: BitAccess,
        viewport: Viewport,
        index: Int,
        updating: Boolean = false,
        current: Location,
        previous: Location,
        step: MovementStep?
    )

    fun BitAccess.updateLocation(viewport: Viewport, index: Int, location: Location) {
        val previous = RegionLocation(viewport.locations[index])
        val current = RegionLocation(location.regionLocation)
        val changed = previous != current
        writeBit(changed)
        if (changed) {
            val regionLocationChange = when {
                previous.x == current.x && previous.z == current.z -> LevelLocationChange
                abs(current.x - previous.x) <= 1 && abs(current.z - previous.z) <= 1 -> PartialLocationChange
                else -> FullLocationChange
            }
            // Write the location change type in bits.
            regionLocationChange.writeBits(this, current.level - previous.level, current.x - previous.x, current.z - previous.z)
            // Update server with new location.
            viewport.locations[index] = current.packedLocation
        }
    }
}

@JvmInline
value class RegionLocation(val packedLocation: Int) {
    val level: Int get() = packedLocation shr 16
    val x: Int get() = packedLocation shr 8
    val z: Int get() = packedLocation and 0xff
}

sealed class RegionLocationChange {
    object LevelLocationChange : RegionLocationChange() {
        override fun writeBits(bits: BitAccess, level: Int, x: Int, z: Int) {
            bits.writeBits(2, 1)
            bits.writeBits(2, level)
        }
    }

    object PartialLocationChange : RegionLocationChange() {
        override fun writeBits(bits: BitAccess, level: Int, x: Int, z: Int) {
            bits.writeBits(2, 2)
            bits.writeBits(5, (level shl 3) or (Direction(x, z).opcodeForPlayerDirection and 0x7))
        }
    }

    object FullLocationChange : RegionLocationChange() {
        override fun writeBits(bits: BitAccess, level: Int, x: Int, z: Int) {
            bits.writeBits(2, 3)
            bits.writeBits(18, (z and 0xff) or (x and 0xff shl 8) or (level shl 16))
        }
    }

    abstract fun writeBits(bits: BitAccess, level: Int, x: Int, z: Int)
}

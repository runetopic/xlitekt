package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Adding
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Moving
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Removing
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Teleporting
import script.packet.assembler.PlayerInfoAssembler.ActivityUpdateType.Updating
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Client.Companion.world
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.location.withinDistance
import xlitekt.shared.buffer.BitAccess
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.buffer.writeBytes
import kotlin.math.abs

/**
 * @author Jordan Abraham
 */
onPacketAssembler<PlayerInfoPacket>(opcode = 80, size = -2) {
    buildPacket {
        val blocks = BytePacketBuilder()
        viewport.also {
            highDefinition(it, blocks, updates, previousLocations, locations, steps, true)
            highDefinition(it, blocks, updates, previousLocations, locations, steps, false)
            lowDefinition(it, blocks, locations, true)
            lowDefinition(it, blocks, locations, false)
        }.update()
        writePacket(blocks.build())
    }
}

fun BytePacketBuilder.highDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    updates: Map<Player, ByteReadPacket>,
    previousLocations: Map<Player, Location?>,
    locations: Map<Player, Location>,
    steps: Map<Player, MovementStep?>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    repeat(viewport.highDefinitionsCount) {
        val index = viewport.highDefinitions[it]
        if (viewport.isNsn(index) == nsn) return@repeat
        val other = viewport.players[index]
        // Check the activities this player is doing.
        val activity = highDefinitionActivities(viewport, other, locations, updates, steps)
        if (other == null || activity == null) {
            viewport.setNsn(index)
            skip++
            return@repeat
        }
        if (skip > -1) {
            writeSkip(skip)
            skip = -1
        }
        writeBit(true)
        val updating = updates[other] != null
        val current = locations[other] ?: other.location
        val previous = previousLocations[other] ?: other.location
        activity.writeBits(this@withBitAccess, viewport, index, updating, current, previous, steps[other])
        when (activity) {
            Removing -> {
                viewport.localPlayersCount = viewport.localPlayersCount - 1
                viewport.players[index] = null
            }
            Teleporting, Moving, Updating -> {
                if (activity != Updating) {
                    // Update the location of the player if they are moving.
                    viewport.locations[index] = current.regionLocation
                }
                if (updating) {
                    blocks.writeBytes(updates[other]!!.copy().readBytes())
                }
            }
            else -> throw IllegalStateException("High definition player had an activity type of $activity.")
        }
    }
    if (skip > -1) writeSkip(skip)
}

fun BytePacketBuilder.lowDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    locations: Map<Player, Location>,
    nsn: Boolean
) = withBitAccess {
    var skip = -1
    repeat(viewport.lowDefinitionsCount) {
        val index = viewport.lowDefinitions[it]
        if (!viewport.isNsn(index) == nsn) return@repeat
        val other = world.players[index]
        // Check the activities this player is doing.
        val activity = lowDefinitionActivities(viewport, other, locations)
        if (other == null || activity == null) {
            viewport.setNsn(index)
            skip++
            return@repeat
        }
        if (skip > -1) {
            writeSkip(skip)
            skip = -1
        }
        writeBit(true)
        activity.writeBits(this@withBitAccess, viewport, index, current = locations[other] ?: other.location, previous = locations[other] ?: other.location)
        when (activity) {
            Adding -> {
                // When adding a player to the local view, we can grab their blocks from their cached list.
                blocks.writeBytes(other.cachedUpdates().keys.toList().buildPlayerUpdateBlocks(other, false).readBytes())
                viewport.players[other.index] = other
                viewport.setNsn(index)
                viewport.localPlayersCount = viewport.localPlayersCount + 1
            }
            else -> throw IllegalStateException("Low definition player had an activity type of $activity.")
        }
    }
    if (skip > -1) writeSkip(skip)
}

fun BitAccess.writeSkip(count: Int) {
    writeBit(false)
    when {
        count == 0 -> writeBits(2, 0)
        count < 32 -> {
            writeBits(2, 1)
            writeBits(5, count)
        }
        count < 256 -> {
            writeBits(2, 2)
            writeBits(8, count)
        }
        count < 2048 -> {
            writeBits(2, 3)
            writeBits(11, count)
        }
    }
}

fun highDefinitionActivities(
    viewport: Viewport,
    other: Player?,
    locations: Map<Player, Location>,
    updates: Map<Player, ByteReadPacket>,
    steps: Map<Player, MovementStep?>
): ActivityUpdateType? {
    val ourLocation = locations[viewport.player]
    val theirLocation = locations[other]
    return when {
        ourLocation != null && (theirLocation == null || !theirLocation.withinDistance(ourLocation)) -> Removing
        steps[other]?.speed == MovementSpeed.TELEPORTING -> Teleporting
        steps[other] != null -> Moving
        updates[other] != null -> Updating
        else -> null
    }
}

fun lowDefinitionActivities(
    viewport: Viewport,
    other: Player?,
    locations: Map<Player, Location>
): ActivityUpdateType? {
    val ourLocation = locations[viewport.player]
    val theirLocation = locations[other]
    return when {
        ourLocation != null && theirLocation != null && theirLocation.withinDistance(ourLocation)/* && viewport.localPlayersCount < 250*/ -> Adding
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
            val running = step!!.speed.isRunning()
            // If the player has pending block updates.
            bits.writeBit(updating)
            // Make the player walk or run.
            bits.writeBits(2, if (running) 2 else 1)
            bits.writeBits(if (running) 4 else 3, step.direction.opcode(running))
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
        step: MovementStep? = null
    )

    fun BitAccess.updateLocation(viewport: Viewport, index: Int, location: Location) {
        val current = viewport.locations[index]
        when (val next = location.regionLocation) {
            current -> writeBit(false)
            else -> {
                writeLocation(current, next)
                viewport.locations[index] = next
            }
        }
    }

    private fun BitAccess.writeLocation(previous: Int, current: Int) {
        writeBit(true)
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
                writeBits(2, 1)
                writeBits(2, deltaLevel)
            }
            abs(currentX - previousX) <= 1 && abs(currentZ - previousZ) <= 1 -> {
                val opcode = Direction.directionFromDelta(deltaX, deltaZ).opcode()
                writeBits(2, 2)
                writeBits(5, (deltaLevel shl 3) or (opcode and 0x7))
            }
            else -> {
                writeBits(2, 3)
                writeBits(18, (deltaZ and 0xff) or (deltaX and 0xff shl 8) or (deltaLevel shl 16))
            }
        }
    }
}

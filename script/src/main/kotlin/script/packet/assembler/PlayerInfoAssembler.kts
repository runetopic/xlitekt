package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.Moving
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.Removing
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.Teleporting
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.Updating
import script.packet.assembler.PlayerInfoAssembler.LowDefinitionActivity.Adding
import xlitekt.game.actor.movement.Direction
import xlitekt.game.actor.movement.MovementSpeed
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Client.Companion.world
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.actor.render.Render
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
sealed class HighDefinitionActivity {
    object Removing : HighDefinitionActivity() {
        override fun writeBits(bits: BitAccess, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            // Player has no update.
            bits.writeBit(false)
            // The player is not moving.
            bits.writeBits(2, 0)
        }
    }

    object Teleporting : HighDefinitionActivity() {
        override fun writeBits(bits: BitAccess, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
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
                bits.writeBits(12, deltaZ + (deltaX shl 5) + (deltaLevel shl 10))
            } else {
                bits.writeBit(true)
                bits.writeBits(30, (deltaZ and 0x3fff) + (deltaX and 0x3fff shl 14) + (deltaLevel and 0x3 shl 28))
            }
        }
    }

    object Moving : HighDefinitionActivity() {
        override fun writeBits(bits: BitAccess, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            val running = step!!.speed.isRunning()
            // If the player has pending block updates.
            bits.writeBit(updating)
            // Make the player walk or run.
            bits.writeBits(2, if (running) 2 else 1)
            bits.writeBits(if (running) 4 else 3, step.direction.opcode(running))
        }
    }

    object Updating : HighDefinitionActivity() {
        override fun writeBits(bits: BitAccess, updating: Boolean, current: Location, previous: Location, step: MovementStep?) {
            // The player has pending block updates.
            bits.writeBit(true)
            // The player is not moving.
            bits.writeBits(2, 0)
        }
    }

    abstract fun writeBits(bits: BitAccess, updating: Boolean, current: Location, previous: Location, step: MovementStep?)
}

sealed class LowDefinitionActivity {
    object Adding : LowDefinitionActivity() {
        override fun writeBits(bits: BitAccess, current: Location) {
            bits.writeBits(2, 0)
            // Update the player location.
            bits.writeBits(13, current.x)
            bits.writeBits(13, current.z)
            // Update the player blocks.
            bits.writeBit(true)
        }
    }

    abstract fun writeBits(bits: BitAccess, current: Location)
}

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
) {
    var skip = -1
    withBitAccess {
        repeat(viewport.highDefinitionsCount) {
            val index = viewport.highDefinitions[it]
            if (viewport.isNsn(index) == nsn) return@repeat
            val other = viewport.players[index]
            // Check the activities this player is doing.
            val activities = highDefinitionActivities(viewport, other, locations, updates, steps)
            if (other == null || activities.isEmpty()) {
                viewport.setNsn(index)
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            // This player has activity and needs to be processed.
            writeBit(true)
            // We can always grab the first one. Ordering is important for this and how they are added to the activities list.
            val activity = activities.first()
            val updating = activities.contains(Updating)
            activity.writeBits(
                bits = this@withBitAccess,
                updating = updating,
                current = locations[other] ?: other.location,
                previous = previousLocations[other] ?: other.location,
                step = steps[other]
            )
            when (activity) {
                Removing -> {
                    // Update location.
                    viewport.locations[index] = 0
                    updateLocation(viewport, index, locations[other] ?: other.location)
                    viewport.players[index] = null
                }
                Teleporting, Moving, Updating -> if (updating) {
                    blocks.writeBytes(updates[other]!!.copy().readBytes())
                }
            }
        }
        if (skip > -1) {
            writeSkip(skip)
        }
    }
}

fun BytePacketBuilder.lowDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    locations: Map<Player, Location>,
    nsn: Boolean
) {
    var skip = -1
    withBitAccess {
        repeat(viewport.lowDefinitionsCount) {
            val index = viewport.lowDefinitions[it]
            if (!viewport.isNsn(index) == nsn) return@repeat
            val other = world.players[index]
            // Check the activities this player is doing.
            val activities = lowDefinitionActivities(viewport, other, locations)
            if (other == null || activities.isEmpty()) {
                viewport.setNsn(index)
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            // This player has activity and needs to be processed.
            writeBit(true)
            // We can always grab the first one.
            val activity = activities.first()
            activity.writeBits(
                bits = this@withBitAccess,
                current = locations[other] ?: other.location
            )
            when (activity) {
                Adding -> {
                    // When adding a player to the local view, we can grab their blocks from their cached list.
                    other.cachedUpdates().filter { entry ->
                        entry.key is Render.Appearance ||
                            entry.key is Render.MovementType ||
                            entry.key is Render.TemporaryMovementType ||
                            entry.key is Render.FaceDirection
                    }.map(Map.Entry<Render, ByteReadPacket>::key).buildPlayerUpdateBlocks(other, false).run { blocks.writeBytes(readBytes()) }
                    viewport.players[other.index] = other
                    viewport.setNsn(index)
                }
            }
        }
        if (skip > -1) {
            writeSkip(skip)
        }
    }
}

fun BitAccess.updateLocation(viewport: Viewport, index: Int, location: Location) {
    writeLocation(viewport.locations[index], location.regionLocation)
    viewport.locations[index] = location.regionLocation
}

fun BitAccess.writeLocation(previous: Int, current: Int) {
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
            writeBits(5, (deltaLevel shl 3) + (opcode and 0x7))
        }
        else -> {
            writeBits(2, 3)
            writeBits(18, (deltaZ and 0xff) + (deltaX and 0xff shl 8) + (deltaLevel shl 16))
        }
    }
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
) = buildList {
    val ourLocation = locations[viewport.player]
    val theirLocation = locations[other]
    if (ourLocation != null && (theirLocation == null || !theirLocation.withinDistance(ourLocation))) add(Removing)
    else if (steps[other]?.speed == MovementSpeed.TELEPORTING) add(Teleporting)
    else if (steps[other] != null) add(Moving)
    // Always check if this player has block updates.
    if (updates[other] != null) add(Updating)
}

fun lowDefinitionActivities(
    viewport: Viewport,
    other: Player?,
    locations: Map<Player, Location>
) = buildList<LowDefinitionActivity> {
    val ourLocation = locations[viewport.player]
    val theirLocation = locations[other]
    if ((ourLocation != null && theirLocation != null && theirLocation.withinDistance(ourLocation))) add(Adding)
    // List is unnecessary here but looks better for consistency.
}

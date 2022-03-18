package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.MOVING
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.REMOVING
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.TELEPORTING
import script.packet.assembler.PlayerInfoAssembler.HighDefinitionActivity.UPDATING
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
enum class HighDefinitionActivity {
    REMOVING,
    TELEPORTING,
    MOVING,
    UPDATING
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
            if (viewport.isNsnFlagged(index) == nsn) return@repeat
            val other = viewport.players[index]
            // Check the activities this player is doing.
            val activities = highDefinitionActivities(viewport, other, locations, updates, steps)
            if (other == null || activities.isEmpty()) {
                viewport.setNsnFlagged(index)
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            // This player has activity and needs to be processed.
            writeBit(true)
            // Check if this player has pending update blocks.
            val updating = activities.contains(UPDATING)
            // We can always grab the first one. Ordering is important for this and how they are added to the activities list.
            when (activities.first()) {
                REMOVING -> {
                    // Player has no update.
                    writeBit(false)
                    // Make the player update.
                    writeBits(2, 0)
                    // Update location.
                    viewport.locations[index] = 0
                    updateLocation(viewport, index, locations[other] ?: other.location)
                    viewport.players[index] = null
                }
                TELEPORTING -> {
                    val current = locations[other] ?: other.location
                    val previous = previousLocations[other] ?: other.location
                    // If the player has pending block updates.
                    writeBit(updating)
                    // Make the player teleport.
                    writeBits(2, 3)
                    var deltaX = current.x - previous.x
                    var deltaZ = current.z - previous.z
                    val deltaLevel = current.level - previous.level
                    if (abs(current.x - previous.x) <= 14 && abs(current.z - previous.z) <= 14) {
                        writeBit(false)
                        if (deltaX < 0) deltaX += 32
                        if (deltaZ < 0) deltaZ += 32
                        writeBits(12, deltaZ + (deltaX shl 5) + (deltaLevel shl 10))
                    } else {
                        writeBit(true)
                        writeBits(30, (deltaZ and 0x3fff) + (deltaX and 0x3fff shl 14) + (deltaLevel and 0x3 shl 28))
                    }
                    if (updating) {
                        blocks.writeBytes(updates[other]!!.copy().readBytes())
                    }
                }
                MOVING -> {
                    val step = steps[other]!!
                    val running = step.speed.isRunning()
                    // If the player has pending block updates.
                    writeBit(updating)
                    // Make the player walk or run.
                    writeBits(2, if (running) 2 else 1)
                    writeBits(if (running) 4 else 3, step.direction.opcode(running))
                    if (updating) {
                        blocks.writeBytes(updates[other]!!.copy().readBytes())
                    }
                }
                UPDATING -> {
                    // The player has pending block updates.
                    writeBit(true)
                    // Make the player update.
                    writeBits(2, 0)
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
            if (!viewport.isNsnFlagged(index) == nsn) return@repeat
            val other = world.players[index]
            // Check if player exists and if we should add this player to high definition.
            if (other == null || !shouldAdd(locations[viewport.player], locations[other])) {
                viewport.setNsnFlagged(index)
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            // This player is updating to be added to high definition.
            writeBit(true)
            writeBits(2, 0)
            // Update the player location.
            val location = locations[other]!!
            updateLocation(viewport, index, location)
            writeBits(13, location.x)
            writeBits(13, location.z)
            // Update the player blocks.
            writeBit(true)
            // When adding a player to the local view, we can grab their blocks from their cached list.
            val cached = other.cachedUpdates().filter { entry ->
                entry.key is Render.Appearance ||
                    entry.key is Render.MovementType ||
                    entry.key is Render.TemporaryMovementType ||
                    entry.key is Render.FaceDirection
            }
                .map(Map.Entry<Render, ByteReadPacket>::key)
                .buildPlayerUpdateBlocks(other, false)
            blocks.writeBytes(cached.readBytes())
            viewport.players[other.index] = other
            viewport.setNsnFlagged(index)
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

fun shouldAdd(us: Location?, other: Location?): Boolean = (us != null && other != null && other.withinDistance(us))
fun shouldRemove(us: Location?, other: Location?): Boolean = us != null && (other == null || !other.withinDistance(us))

fun highDefinitionActivities(
    viewport: Viewport,
    other: Player?,
    locations: Map<Player, Location>,
    updates: Map<Player, ByteReadPacket>,
    steps: Map<Player, MovementStep?>
) = buildList {
    if (shouldRemove(locations[viewport.player], locations[other])) add(REMOVING)
    if (steps[other]?.speed == MovementSpeed.TELEPORTING) add(TELEPORTING)
    if (steps[other] != null) add(MOVING)
    if (updates[other] != null) add(UPDATING)
}

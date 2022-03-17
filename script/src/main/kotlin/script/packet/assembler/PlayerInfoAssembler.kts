package script.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
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
        repeat(viewport.localIndexesSize) {
            val index = viewport.localIndexes[it]
            if (nsn == (0x1 and viewport.nsnFlags[index] != 0)) return@repeat
            val other = viewport.localPlayers[index]
            // TODO Do something about this boolean mess.
            val removing = shouldRemove(locations[viewport.player], locations[other])
            val updating = updates[other] != null
            val moving = steps[other] != null
            val teleporting = steps[other]?.speed == MovementSpeed.TELEPORTING
            val active = removing || moving || updating || teleporting
            if (other == null || !active) {
                viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            writeBit(true)
            processHighDefinitionPlayer(
                updating,
                removing,
                moving,
                viewport,
                index,
                other,
                blocks,
                updates[other],
                previousLocations[other],
                locations[other],
                teleporting,
                steps[other]
            )
        }
        if (skip > -1) {
            writeSkip(skip)
        }
    }
}

fun BitAccess.processHighDefinitionPlayer(
    updating: Boolean,
    removing: Boolean,
    moving: Boolean,
    viewport: Viewport,
    index: Int,
    other: Player,
    blocks: BytePacketBuilder,
    updates: ByteReadPacket?,
    previousLocation: Location?,
    otherLocation: Location?,
    teleport: Boolean,
    movementStep: MovementStep?
) {
    when {
        removing -> { // remove the player
            // send a position update
            writeBit(false)
            writeBits(2, 0)
            viewport.locations[index] = 0
            updateLocation(viewport, index, otherLocation ?: other.location)
            viewport.localPlayers[index] = null
        }
        teleport -> {
            val currentLocation = otherLocation ?: other.location
            val previous = previousLocation ?: other.location
            writeBit(updating)
            writeBits(2, 3)
            var xOffset = currentLocation.x - previous.x
            var yOffset = currentLocation.z - previous.z
            val planeOffset = currentLocation.level - previous.level
            if (abs(currentLocation.x - previous.x) <= 14 &&
                abs(currentLocation.z - previous.z) <= 14
            ) {
                writeBits(1, 0)
                if (xOffset < 0) xOffset += 32
                if (yOffset < 0) yOffset += 32
                writeBits(12, yOffset + (xOffset shl 5) + (planeOffset shl 10))
            } else {
                writeBits(1, 1)
                writeBits(30, (yOffset and 0x3fff) + (xOffset and 0x3fff shl 14) + (planeOffset and 0x3 shl 28))
            }

            if (updating) {
                // TODO We can cache appearances here if we really want to.
                blocks.writeBytes(updates!!.copy().readBytes())
            }
        }
        moving -> {
            val step = movementStep!!
            val running = step.speed.isRunning()
            writeBit(updating)
            writeBits(2, if (running) 2 else 1)
            writeBits(if (running) 4 else 3, step.direction.opcode(running))
            if (updating) {
                // TODO We can cache appearances here if we really want to.
                blocks.writeBytes(updates!!.copy().readBytes())
            }
        }
        updating -> {
            // send a block update
            writeBit(true)
            writeBits(2, 0)
            // TODO We can cache appearances here if we really want to.
            blocks.writeBytes(updates!!.copy().readBytes())
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
        repeat(viewport.externalIndexesSize) {
            val index = viewport.externalIndexes[it]
            if (nsn == (0x1 and viewport.nsnFlags[index] == 0)) return@repeat
            val other = world.players[index]
            // TODO Do something about this boolean mess.
            val adding = shouldAdd(locations[viewport.player], locations[other])
            if (other == null || !adding) {
                viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            writeBit(true)
            processLowDefinitionPlayer(
                viewport,
                other,
                index,
                blocks,
                locations[other]!!, // This is okay since it is checked above.
            )
        }
        if (skip > -1) {
            writeSkip(skip)
        }
    }
}

fun BitAccess.processLowDefinitionPlayer(
    viewport: Viewport,
    other: Player,
    index: Int,
    blocks: BytePacketBuilder,
    location: Location,
) {
    // add an external player to start tracking
    writeBits(2, 0)
    updateLocation(viewport, index, location)
    writeBits(13, location.x)
    writeBits(13, location.z)
    // send a force block update
    writeBit(true)

    // When adding a player to the local view, we can grab their blocks from their cached list.
    val cached = other.cachedUpdates()
        .filter {
            it.key is Render.Appearance ||
                it.key is Render.MovementType ||
                it.key is Render.TemporaryMovementType ||
                it.key is Render.FaceDirection
        }
        .map(Map.Entry<Render, ByteReadPacket>::key)
        .buildPlayerUpdateBlocks(other, false)

    blocks.writeBytes(cached.readBytes())

    viewport.localPlayers[other.index] = other
    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
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

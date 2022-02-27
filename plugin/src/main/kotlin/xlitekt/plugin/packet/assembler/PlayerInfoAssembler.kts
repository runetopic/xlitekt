package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.actor.player.Client.Companion.world
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.playerBlocks
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
            highDefinition(it, blocks, updates, locations, true)
            highDefinition(it, blocks, updates, locations, false)
            lowDefinition(it, blocks, locations, true)
            lowDefinition(it, blocks, locations, false)
        }.update()
        writeBytes(blocks.build().readBytes())
    }
}

fun BytePacketBuilder.highDefinition(
    viewport: Viewport,
    blocks: BytePacketBuilder,
    updates: Map<Player, ByteReadPacket>,
    locations: Map<Player, Location>,
    nsn: Boolean
) {
    var skip = -1
    withBitAccess {
        repeat(viewport.localIndexesSize) {
            val index = viewport.localIndexes[it]
            if (nsn == (0x1 and viewport.nsnFlags[index] != 0)) return@repeat
            val other = viewport.localPlayers[index]
            // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
            val removing = shouldRemove(locations[viewport.player], locations[other])
            val updating = updates[other] != null
            val active = removing || updating
            if (!active) {
                viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            writeBit(true)
            processHighDefinitionPlayer(removing, viewport, index, other, updating, blocks, updates)
        }
        if (skip > -1) {
            writeSkip(skip)
        }
    }
}

fun BitAccess.processHighDefinitionPlayer(
    removing: Boolean,
    viewport: Viewport,
    index: Int,
    other: Player?,
    updating: Boolean,
    blocks: BytePacketBuilder,
    updates: Map<Player, ByteReadPacket>
) {
    writeBit(!removing)
    when {
        removing -> { // remove the player
            // send a position update
            writeBits(2, 0)
            viewport.coordinates[index] = 0
            validateCoordinates(viewport, other, index)
            viewport.localPlayers[index] = null
        }
        updating -> {
            // send a block update
            writeBits(2, 0)
            blocks.writeBytes(updates[other]!!.copy().readBytes())
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
            // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
            val adding = shouldAdd(locations[viewport.player], locations[other])
            if (!adding) {
                viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                skip++
                return@repeat
            }
            if (skip > -1) {
                writeSkip(skip)
                skip = -1
            }
            writeBit(true)
            processLowDefinitionPlayer(viewport, other!!, index, blocks)
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
    blocks: BytePacketBuilder
) {
    // add an external player to start tracking
    writeBits(2, 0)
    validateCoordinates(viewport, other, index)
    writeBits(13, other.location.x)
    writeBits(13, other.location.z)
    // send a force block update
    writeBit(true)

    // Send appearance.
    val appearanceBlock = playerBlocks[Render.Appearance::class]!!
    blocks.writeByte(appearanceBlock.mask.toByte())
    blocks.writeBytes(appearanceBlock.build(other, other.appearance).copy().readBytes())

    viewport.localPlayers[other.index] = other
    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
}

fun BitAccess.validateCoordinates(viewport: Viewport, other: Player?, index: Int) {
    val currentPacked = viewport.coordinates[index]
    val packed = other?.location?.regionCoordinates ?: currentPacked
    val updating = other != null && packed != currentPacked
    writeBit(updating)
    if (updating) {
        updateCoordinates(currentPacked, packed)
        viewport.coordinates[index] = packed
    }
}

fun BitAccess.updateCoordinates(lastCoordinates: Int, currentCoordinates: Int) {
    val previousLevel = lastCoordinates shr 16
    val lastRegionX = lastCoordinates shr 8
    val lastRegionZ = lastCoordinates and 0xff

    val currentLevel = currentCoordinates shr 16
    val currentRegionX = currentCoordinates shr 8
    val currentRegionZ = currentCoordinates and 0xff

    val deltaLevel = currentLevel - previousLevel
    val deltaX = currentRegionX - lastRegionX
    val deltaZ = currentRegionZ - lastRegionZ

    when {
        lastRegionX == currentRegionX && lastRegionZ == currentRegionZ -> {
            writeBits(2, 1)
            writeBits(2, deltaLevel)
        }
        abs(currentRegionX - lastRegionX) <= 1 && abs(currentRegionZ - lastRegionZ) <= 1 -> {
            // TODO Extract this directional stuff out.
            val opcode = when {
                deltaX == -1 && deltaZ == -1 -> 0
                deltaX == 1 && deltaZ == -1 -> 2
                deltaX == -1 && deltaZ == 1 -> 5
                deltaX == 1 && deltaZ == 1 -> 7
                deltaZ == -1 -> 1
                deltaX == -1 -> 3
                deltaX == 1 -> 4
                else -> 6
            }
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

fun shouldAdd(us: Location?, other: Location?): Boolean = (us != null && other != null/* && other != viewport.player*/ && other.withinDistance(us))
fun shouldRemove(us: Location?, other: Location?): Boolean = us != null && (other == null || !other.withinDistance(us)/* || !world.players.contains(other)*/)

package com.runetopic.xlitekt.network.packet.write

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.withinDistance
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.write.block.RenderingBlock
import com.runetopic.xlitekt.network.packet.write.block.player.PlayerAppearanceBlock
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.BitAccess
import com.runetopic.xlitekt.util.ext.toInt
import com.runetopic.xlitekt.util.ext.toIntInv
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder
import kotlin.math.abs

class PlayerInfoPacket(private val player: Player) : Packet {
    private val world by inject<World>()

    override fun opcode(): Int = 80
    override fun size(): Int = -2

    override fun builder() = buildPacket {
        val blocks = buildPacket { }
        syncHighDefinition(this, blocks, true)
        syncHighDefinition(this, blocks, false)
        lowDefinition(this, blocks, true)
        lowDefinition(this, blocks, false)
        writePacket(blocks.build())
        player.viewport.shift()
    }

    private fun syncHighDefinition(builder: BytePacketBuilder, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        builder.withBitAccess {
            repeat(player.viewport.localIndexesSize) {
                val index = player.viewport.localIndexes[it]
                if (nsn == (0x1 and player.viewport.nsnFlags[index] != 0)) return@repeat
                if (skip > 0) {
                    player.viewport.nsnFlags[index] = player.viewport.nsnFlags[index] or 2
                    skip--
                    return@repeat
                }
                val other = player.viewport.localPlayers[index]
                val removing = shouldRemove(player, other)
                val updating = shouldUpdate(other)
                val active = removing || updating
                writeBit(active)
                if (active.not()) {
                    skip += skip(player, true, it, nsn)
                    player.viewport.nsnFlags[index] = player.viewport.nsnFlags[index] or 2
                } else {
                    processHighDefinitionPlayer(this, blocks, player, other, index, removing, updating)
                }
            }
        }
    }

    private fun lowDefinition(builder: BytePacketBuilder, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        builder.withBitAccess {
            repeat(player.viewport.externalIndexesSize) {
                val index = player.viewport.externalIndexes[it]
                if (nsn == (0x1 and player.viewport.nsnFlags[index] == 0)) return@repeat
                if (skip > 0) {
                    player.viewport.nsnFlags[index] = player.viewport.nsnFlags[index] or 2
                    skip--
                    return@repeat
                }
                val other = world.players[index]
                val adding = shouldAdd(player, other)
                val active = adding
                writeBit(active)
                if (active.not()) {
                    skip += skip(player, false, it, nsn)
                    player.viewport.nsnFlags[index] = player.viewport.nsnFlags[index] or 2
                } else {
                    processLowDefinitionPlayer(this, blocks, player, other!!, index, adding)
                }
            }
        }
    }

    private fun processHighDefinitionPlayer(
        builder: BitAccess,
        blocks: BytePacketBuilder,
        player: Player,
        other: Player?,
        index: Int,
        removing: Boolean,
        updating: Boolean
    ) {
        builder.writeBits(1, removing.toIntInv())
        when {
            removing -> { // remove the player
                // send a position update
                builder.writeBits(2, 0)
                player.viewport.coordinates[index] = other?.previousTile?.regionCoordinates ?: other?.tile?.regionCoordinates ?: 0
                validateCoordinates(builder, player, other, index)
                player.viewport.localPlayers[index] = null
            }
            updating -> {
                // send a block update
                builder.writeBits(2, 0)
                encodePendingUpdates(other!!, blocks)
            }
        }
    }

    private fun processLowDefinitionPlayer(
        builder: BitAccess,
        blocks: BytePacketBuilder,
        player: Player,
        other: Player,
        index: Int,
        adding: Boolean,
    ) {
        if (adding) {
            // add an external player to start tracking
            builder.writeBits(2, 0)
            validateCoordinates(builder, player, other, index)
            builder.writeBits(13, other.tile.x)
            builder.writeBits(13, other.tile.z)
            // send a force block update
            builder.writeBits(1, 1)
            encodePendingUpdates(other, blocks)
            player.viewport.localPlayers[other.pid] = other
            player.viewport.nsnFlags[index] = player.viewport.nsnFlags[index] or 2
        }
    }

    private fun updateCoordinates(
        builder: BitAccess,
        lastCoordinates: Int,
        currentCoordinates: Int
    ) {
        val lastPlane = lastCoordinates shr 16
        val lastRegionX = lastCoordinates shr 8
        val lastRegionZ = lastCoordinates and 0xFF

        val currentPlane = currentCoordinates shr 16
        val currentRegionX = currentCoordinates shr 8
        val currentRegionZ = currentCoordinates and 0xFF

        val deltaPlane = currentPlane - lastPlane
        val deltaX = currentRegionX - lastRegionX
        val deltaZ = currentRegionZ - lastRegionZ

        if (lastRegionX == currentRegionX && lastRegionZ == currentRegionZ) {
            builder.writeBits(2, 1)
            builder.writeBits(2, deltaPlane)
        } else if (abs(currentRegionX - lastRegionX) <= 1 && abs(currentRegionZ - lastRegionZ) <= 1) {
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
            builder.writeBits(2, 2)
            builder.writeBits(5, (deltaPlane shl 3) + (opcode and 0x7))
        } else {
            builder.writeBits(2, 3)
            builder.writeBits(18, (deltaZ and 0xFF) + (deltaX and 0xFF shl 8) + (deltaPlane shl 16))
        }
    }

    private fun validateCoordinates(
        builder: BitAccess,
        player: Player,
        other: Player?,
        index: Int
    ) {
        val currentPacked = player.viewport.coordinates[index]
        val packed = other?.tile?.regionCoordinates ?: currentPacked
        val updating = other != null && packed != currentPacked
        builder.writeBits(1, updating.toInt())
        if (updating) {
            updateCoordinates(builder, currentPacked, packed)
            player.viewport.coordinates[index] = packed
        }
    }

    private fun BitAccess.skip(
        player: Player,
        local: Boolean,
        offset: Int,
        nsn: Boolean
    ): Int {
        var count = 0
        if (local) {
            (offset + 1 until player.viewport.localIndexesSize)
                .asSequence()
                .map { player.viewport.localIndexes[it] }
                .filter { nsn != (0x1 and player.viewport.nsnFlags[it] != 0) }
                .map { player.viewport.localPlayers[it] }
                .takeWhile { shouldRemove(player, it).not() }
                .takeWhile { (it != null && it.renderer.hasPendingUpdate()).not() }
                .forEach { _ -> count++ }
        } else {
            (offset + 1 until player.viewport.externalIndexesSize)
                .asSequence()
                .map { player.viewport.externalIndexes[it] }
                .filter { nsn != (0x1 and player.viewport.nsnFlags[it] == 0) }
                .map { world.players[it] }
                .takeWhile { shouldAdd(player, it).not() }
                .forEach { _ -> count++ }
        }

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
        return count
    }

    private fun encodePendingUpdates(other: Player, blocks: BytePacketBuilder) {
        val updates = other.renderer.pendingUpdates.map { mapToBlock(it) }.sortedWith(compareBy { it.second.keyPair().first }).toMap()
        var mask = 0x0
        updates.forEach {
            mask = mask or it.value.keyPair().second
        }
        if (mask >= 0xFF) {
            mask = mask or 0x10
        }
        blocks.writeByte(mask.toByte())
        if (mask >= 0xFF) {
            blocks.writeByte((mask shr 8).toByte())
        }
        updates.forEach {
            blocks.writePacket(it.value.build(other, it.key))
        }
    }

    @Suppress("UNCHECKED_CAST")
    private fun mapToBlock(it: Render) = when (it) {
        is Render.Appearance -> Pair(it, PlayerAppearanceBlock() as RenderingBlock<Player, Render>)
        else -> throw RuntimeException()
    }

    private fun shouldUpdate(other: Player?): Boolean = other?.renderer?.hasPendingUpdate() ?: false
    private fun shouldAdd(player: Player, other: Player?): Boolean = (other != null && other != player && other.tile.withinDistance(player))
    private fun shouldRemove(player: Player, other: Player?): Boolean = (other == null || other.tile.withinDistance(player).not())
}

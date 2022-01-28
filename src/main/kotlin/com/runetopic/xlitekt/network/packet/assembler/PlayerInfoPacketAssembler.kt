package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.withinDistance
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerAppearanceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerSequenceBlock
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.BitAccess
import com.runetopic.xlitekt.util.ext.toInt
import com.runetopic.xlitekt.util.ext.toIntInv
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder
import kotlin.math.abs

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class PlayerInfoPacketAssembler : PacketAssembler<PlayerInfoPacket>(opcode = 80, size = -2) {

    private val world by inject<World>()

    override fun assemblePacket(packet: PlayerInfoPacket) = buildPacket {
        val blocks = buildPacket { }
        packet.player.let {
            highDefinition(it, this, blocks, true)
            highDefinition(it, this, blocks, false)
            lowDefinition(it, this, blocks, true)
            lowDefinition(it, this, blocks, false)
            writePacket(blocks.build())
            it.viewport.shift()
        }
    }

    private fun highDefinition(player: Player, builder: BytePacketBuilder, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        builder.withBitAccess {
            player.viewport.let { viewport ->
                repeat(viewport.localIndexesSize) {
                    val index = viewport.localIndexes[it]
                    if (nsn == (0x1 and viewport.nsnFlags[index] != 0)) return@repeat
                    if (skip > 0) {
                        viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                        skip--
                        return@repeat
                    }
                    val other = viewport.localPlayers[index]
                    val removing = shouldRemove(player, other)
                    val updating = shouldUpdate(other)
                    val active = removing || updating
                    writeBit(active)
                    if (active.not()) {
                        skip += skip(player, true, it, nsn)
                        viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                    } else {
                        processHighDefinitionPlayer(this, blocks, player, other, index, removing, updating)
                    }
                }
            }
        }
    }

    private fun lowDefinition(player: Player, builder: BytePacketBuilder, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        builder.withBitAccess {
            player.viewport.let { viewport ->
                repeat(viewport.externalIndexesSize) {
                    val index = viewport.externalIndexes[it]
                    if (nsn == (0x1 and viewport.nsnFlags[index] == 0)) return@repeat
                    if (skip > 0) {
                        viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                        skip--
                        return@repeat
                    }
                    val other = world.players[index]
                    val adding = shouldAdd(player, other)
                    writeBit(adding)
                    if (adding.not()) {
                        skip += skip(player, false, it, nsn)
                        viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                    } else {
                        processLowDefinitionPlayer(this, blocks, player, other!!, index, adding)
                    }
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
                encodePendingBlocks(false, other!!, blocks)
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
            encodePendingBlocks(true, other, blocks)
            player.viewport.localPlayers[other.index] = other
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
        val lastRegionZ = lastCoordinates and 0xff

        val currentPlane = currentCoordinates shr 16
        val currentRegionX = currentCoordinates shr 8
        val currentRegionZ = currentCoordinates and 0xff

        val deltaPlane = currentPlane - lastPlane
        val deltaX = currentRegionX - lastRegionX
        val deltaZ = currentRegionZ - lastRegionZ

        if (lastRegionX == currentRegionX && lastRegionZ == currentRegionZ) {
            builder.writeBits(2, 1)
            builder.writeBits(2, deltaPlane)
        } else if (abs(currentRegionX - lastRegionX) <= 1 && abs(currentRegionZ - lastRegionZ) <= 1) {
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
            builder.writeBits(2, 2)
            builder.writeBits(5, (deltaPlane shl 3) + (opcode and 0x7))
        } else {
            builder.writeBits(2, 3)
            builder.writeBits(18, (deltaZ and 0xff) + (deltaX and 0xff shl 8) + (deltaPlane shl 16))
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

        player.viewport.let {
            if (local) {
                for (index in offset + 1 until it.localIndexesSize) {
                    val localIndex = it.localIndexes[index]
                    if (nsn == (0x1 and it.nsnFlags[localIndex] != 0)) continue
                    val localPlayer = it.localPlayers[index]
                    if (shouldRemove(player, localPlayer) || (localPlayer != null && localPlayer.renderer.hasPendingUpdate())) break
                    count++
                }
            } else {
                for (index in offset + 1 until it.externalIndexesSize) {
                    val externalIndex = it.externalIndexes[index]
                    if (nsn == (0x1 and it.nsnFlags[externalIndex] == 0)) continue
                    val externalPlayer = world.players[externalIndex]
                    if (shouldAdd(player, externalPlayer)) break
                    count++
                }
            }
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

    private fun encodePendingBlocks(forceOtherUpdate: Boolean, other: Player, blocks: BytePacketBuilder) {
        if (forceOtherUpdate) other.renderer.appearance(Render.Appearance.Gender.MALE, -1, -1, false)
        val updates = other.renderer.pendingUpdates.map { mapToBlock(it) }.sortedWith(compareBy { it.second.index }).toMap()
        var mask = 0x0
        updates.forEach { mask = mask or it.value.mask }
        if (mask >= 0xff) { mask = mask or 0x10 }
        blocks.writeByte(mask.toByte())
        if (mask >= 0xff) { blocks.writeByte((mask shr 8).toByte()) }
        updates.forEach { blocks.writePacket(it.value.build(other, it.key)) }
    }

    private fun mapToBlock(it: Render) = when (it) {
        is Render.Appearance -> it to PlayerAppearanceBlock()
        is Render.Animation -> it to PlayerSequenceBlock()
        else -> throw IllegalStateException("Unhandled player block in PlayerInfo. Block was $it")
    }

    private fun shouldUpdate(other: Player?): Boolean = other?.renderer?.hasPendingUpdate() ?: false
    private fun shouldAdd(player: Player, other: Player?): Boolean = (other != null && other != player && other.tile.withinDistance(player))
    private fun shouldRemove(player: Player, other: Player?): Boolean = (other == null || other.tile.withinDistance(player).not() || world.players.contains(other).not())
}

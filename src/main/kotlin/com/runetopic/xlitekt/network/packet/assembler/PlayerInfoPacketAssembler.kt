package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.withinDistance
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerAppearanceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerFaceActorBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerForceMovementBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerHitDamageBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerSequenceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerTemporaryMovementTypeBlock
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
            highDefinition(it, blocks, true)
            highDefinition(it, blocks, false)
            lowDefinition(it, blocks, true)
            lowDefinition(it, blocks, false)
            writePacket(blocks.build())
            it.viewport.shift()
        }
    }

    private fun BytePacketBuilder.highDefinition(player: Player, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        withBitAccess {
            val viewport = player.viewport
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
                    processHighDefinitionPlayer(removing, player, index, other, updating, blocks)
                }
            }
        }
    }

    private fun BitAccess.processHighDefinitionPlayer(
        removing: Boolean,
        player: Player,
        index: Int,
        other: Player?,
        updating: Boolean,
        blocks: BytePacketBuilder
    ) {
        writeBits(1, removing.toIntInv())
        when {
            removing -> { // remove the player
                // send a position update
                writeBits(2, 0)
                player.viewport.coordinates[index] = other?.previousTile?.regionCoordinates ?: other?.tile?.regionCoordinates ?: 0
                validateCoordinates(this, player, other, index)
                player.viewport.localPlayers[index] = null
            }
            updating -> {
                // send a block update
                writeBits(2, 0)
                encodePendingBlocks(false, other!!, blocks)
            }
        }
    }

    private fun BytePacketBuilder.lowDefinition(player: Player, blocks: BytePacketBuilder, nsn: Boolean) {
        var skip = 0
        withBitAccess {
            val viewport = player.viewport
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
                    processLowDefinitionPlayer(adding, player, other!!, index, blocks)
                }
            }
        }
    }

    private fun BitAccess.processLowDefinitionPlayer(
        adding: Boolean,
        player: Player,
        other: Player,
        index: Int,
        blocks: BytePacketBuilder
    ) {
        if (adding) {
            // add an external player to start tracking
            writeBits(2, 0)
            validateCoordinates(this, player, other, index)
            writeBits(13, other.tile.x)
            writeBits(13, other.tile.z)
            // send a force block update
            writeBits(1, 1)
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

        val viewport = player.viewport

        if (local) {
            for (index in offset + 1 until viewport.localIndexesSize) {
                val localIndex = viewport.localIndexes[index]
                if (nsn == (0x1 and viewport.nsnFlags[localIndex] != 0)) continue
                val localPlayer = viewport.localPlayers[localIndex]
                if (shouldRemove(player, localPlayer) || (localPlayer != null && localPlayer.hasPendingUpdate())) break
                count++
            }
        } else {
            for (index in offset + 1 until viewport.externalIndexesSize) {
                val externalIndex = viewport.externalIndexes[index]
                if (nsn == (0x1 and viewport.nsnFlags[externalIndex] == 0)) continue
                val externalPlayer = world.players[externalIndex]
                if (shouldAdd(player, externalPlayer)) break
                count++
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
        if (forceOtherUpdate) other.refreshAppearance(other.appearance)
        val updates = other.pendingUpdates().map { mapToBlock(it) }.sortedWith(compareBy { it.second.index }).toMap()
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
        is Render.FaceActor -> it to PlayerFaceActorBlock()
        is Render.ForceMovement -> it to PlayerForceMovementBlock()
        is Render.HitDamage -> it to PlayerHitDamageBlock()
        is Render.TemporaryMovementType -> it to PlayerTemporaryMovementTypeBlock()
        else -> throw IllegalStateException("Unhandled player block in PlayerInfo. Block was $it")
    }

    private fun shouldUpdate(other: Player?): Boolean = other?.hasPendingUpdate() ?: false
    private fun shouldAdd(player: Player, other: Player?): Boolean = (other != null && other != player && other.tile.withinDistance(player))
    private fun shouldRemove(player: Player, other: Player?): Boolean = (other == null || !other.tile.withinDistance(player) || !world.players.contains(other))
}

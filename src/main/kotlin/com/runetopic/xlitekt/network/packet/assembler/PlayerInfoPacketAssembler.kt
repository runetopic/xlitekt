package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.game.world.map.location.withinDistance
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerAppearanceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerFaceActorBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerFaceDirectionBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerForceMovementBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerHitDamageBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerMovementTypeBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerOverheadChatBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerPublicChatBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerRecolorBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerSequenceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerSpotAnimationBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerTemporaryMovementTypeBlock
import com.runetopic.xlitekt.network.packet.assembler.block.player.PlayerUsernameOverrideBlock
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.BitAccess
import com.runetopic.xlitekt.shared.buffer.withBitAccess
import com.runetopic.xlitekt.shared.toInt
import com.runetopic.xlitekt.shared.toIntInv
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import kotlin.math.abs

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class PlayerInfoPacketAssembler : PacketAssembler<PlayerInfoPacket>(opcode = 80, size = -2) {

    private val world by inject<World>()

    override fun assemblePacket(packet: PlayerInfoPacket) = buildPacket {
        val updates = packet.updates
        val blocks = BytePacketBuilder()
        packet.player.viewport.also {
            highDefinition(it, blocks, updates, true)
            highDefinition(it, blocks, updates, false)
            lowDefinition(it, blocks, updates, true)
            lowDefinition(it, blocks, updates, false)
        }.update()
        writePacket(blocks.build())
    }

    private fun BytePacketBuilder.highDefinition(viewport: Viewport, blocks: BytePacketBuilder, updates: List<Render>, nsn: Boolean) {
        var skip = 0
        withBitAccess {
            repeat(viewport.localIndexesSize) {
                val index = viewport.localIndexes[it]
                if (nsn == (0x1 and viewport.nsnFlags[index] != 0)) return@repeat
                if (skip > 0) {
                    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                    skip--
                    return@repeat
                }
                val other = viewport.localPlayers[index]
                // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
                val removing = shouldRemove(viewport, other)
                val updating = shouldUpdate(other)
                val active = removing || updating
                writeBit(active)
                if (active.not()) {
                    skip += skip(viewport, true, it, nsn)
                    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                } else {
                    processHighDefinitionPlayer(removing, viewport, index, other, updating, blocks, updates)
                }
            }
        }
    }

    private fun BitAccess.processHighDefinitionPlayer(
        removing: Boolean,
        viewport: Viewport,
        index: Int,
        other: Player?,
        updating: Boolean,
        blocks: BytePacketBuilder,
        updates: List<Render>
    ) {
        writeBits(1, removing.toIntInv())
        when {
            removing -> { // remove the player
                // send a position update
                writeBits(2, 0)
                viewport.coordinates[index] = other?.previousLocation?.regionCoordinates ?: other?.location?.regionCoordinates ?: 0
                validateCoordinates(viewport, other, index)
                viewport.localPlayers[index] = null
            }
            updating -> {
                // send a block update
                writeBits(2, 0)
                blocks.encodePendingBlocks(false, other!!, updates)
            }
        }
    }

    private fun BytePacketBuilder.lowDefinition(viewport: Viewport, blocks: BytePacketBuilder, updates: List<Render>, nsn: Boolean) {
        var skip = 0
        withBitAccess {
            repeat(viewport.externalIndexesSize) {
                val index = viewport.externalIndexes[it]
                if (nsn == (0x1 and viewport.nsnFlags[index] == 0)) return@repeat
                if (skip > 0) {
                    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                    skip--
                    return@repeat
                }
                val other = world.players[index]
                // TODO Extract this out into an enum or something instead of passing around a bunch of booleans.
                val adding = shouldAdd(viewport, other)
                writeBit(adding)
                if (adding.not()) {
                    skip += skip(viewport, false, it, nsn)
                    viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
                } else {
                    processLowDefinitionPlayer(adding, viewport, other!!, index, blocks, updates)
                }
            }
        }
    }

    private fun BitAccess.processLowDefinitionPlayer(
        adding: Boolean,
        viewport: Viewport,
        other: Player,
        index: Int,
        blocks: BytePacketBuilder,
        updates: List<Render>
    ) {
        if (adding) {
            // add an external player to start tracking
            writeBits(2, 0)
            validateCoordinates(viewport, other, index)
            writeBits(13, other.location.x)
            writeBits(13, other.location.z)
            // send a force block update
            writeBits(1, 1)
            blocks.encodePendingBlocks(true, other, updates)
            viewport.localPlayers[other.index] = other
            viewport.nsnFlags[index] = viewport.nsnFlags[index] or 2
        }
    }

    private fun BitAccess.validateCoordinates(viewport: Viewport, other: Player?, index: Int) {
        val currentPacked = viewport.coordinates[index]
        val packed = other?.location?.regionCoordinates ?: currentPacked
        val updating = other != null && packed != currentPacked
        writeBits(1, updating.toInt())
        if (updating) {
            updateCoordinates(currentPacked, packed)
            viewport.coordinates[index] = packed
        }
    }

    private fun BitAccess.updateCoordinates(lastCoordinates: Int, currentCoordinates: Int) {
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

    private fun BitAccess.skip(
        viewport: Viewport,
        local: Boolean,
        offset: Int,
        nsn: Boolean
    ): Int {
        var count = 0
        if (local) {
            for (index in offset + 1 until viewport.localIndexesSize) {
                val localIndex = viewport.localIndexes[index]
                if (nsn == (0x1 and viewport.nsnFlags[localIndex] != 0)) continue
                val localPlayer = viewport.localPlayers[localIndex]
                if (shouldRemove(viewport, localPlayer) || (localPlayer != null && localPlayer.hasPendingUpdate())) break
                count++
            }
        } else {
            for (index in offset + 1 until viewport.externalIndexesSize) {
                val externalIndex = viewport.externalIndexes[index]
                if (nsn == (0x1 and viewport.nsnFlags[externalIndex] == 0)) continue
                val externalPlayer = world.players[externalIndex]
                if (shouldAdd(viewport, externalPlayer)) break
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

    private fun BytePacketBuilder.encodePendingBlocks(forceOtherUpdate: Boolean, other: Player, updates: List<Render>) {
        if (forceOtherUpdate) {
            other.updateAppearance()
            other.nextTick = true
        }
        val blocks = updates.map { it to renderingBlockMap[it::class]!! }.sortedBy { it.second.index }.toMap()
        val mask = blocks.map { it.value.mask }.sum().let { if (it > 0xff) it or 0x10 else it }
        if (mask > 0xff) writeShortLittleEndian(mask.toShort()) else writeByte(mask.toByte())
        blocks.forEach { writePacket(it.value.build(other, it.key)) }
    }

    private fun shouldUpdate(other: Player?): Boolean = other?.hasPendingUpdate() ?: false
    private fun shouldAdd(viewport: Viewport, other: Player?): Boolean = (other != null && other != viewport.player && other.location.withinDistance(viewport.player))
    private fun shouldRemove(viewport: Viewport, other: Player?): Boolean = (other == null || !other.location.withinDistance(viewport.player) || !world.players.contains(other))

    private companion object {
        val renderingBlockMap = mapOf(
            Render.Appearance::class to PlayerAppearanceBlock(),
            Render.Sequence::class to PlayerSequenceBlock(),
            Render.UsernameOverride::class to PlayerUsernameOverrideBlock(),
            Render.FaceActor::class to PlayerFaceActorBlock(),
            Render.FaceDirection::class to PlayerFaceDirectionBlock(),
            Render.MovementType::class to PlayerMovementTypeBlock(),
            Render.ForceMovement::class to PlayerForceMovementBlock(),
            Render.HitDamage::class to PlayerHitDamageBlock(),
            Render.OverheadChat::class to PlayerOverheadChatBlock(),
            Render.PublicChat::class to PlayerPublicChatBlock(),
            Render.Recolor::class to PlayerRecolorBlock(),
            Render.SpotAnimation::class to PlayerSpotAnimationBlock(),
            Render.TemporaryMovementType::class to PlayerTemporaryMovementTypeBlock()
        )
    }
}

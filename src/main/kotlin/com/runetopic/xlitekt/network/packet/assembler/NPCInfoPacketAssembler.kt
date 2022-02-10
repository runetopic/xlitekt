package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.withinDistance
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCCustomLevelBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCFaceActorBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCFaceTileBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCForceMovementBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCHitDamageBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCOverheadChatBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCRecolorBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCSequenceBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCSpotAnimationBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.NPCTransmogrificationBlock
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.BitAccess
import com.runetopic.xlitekt.util.ext.toInt
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder

/**
 * @author Tyler Telis
 */
class NPCInfoPacketAssembler(
    private val extendedViewport: Boolean,
) : PacketAssembler<NPCInfoPacket>(opcode = if (extendedViewport) 90 else 78, size = -2) {

    private val world by inject<World>()

    override fun assemblePacket(packet: NPCInfoPacket) = buildPacket {
        val blocks = buildPacket {}

        withBitAccess {
            packet.player.viewport.let {
                writeBits(8, it.localNPCs.size)
                highDefinition(it, blocks)
                lowDefinition(it, blocks)
            }

            if (blocks.size > 0) {
                writeBits(15, Short.MAX_VALUE.toInt())
            }
        }

        writePacket(blocks.build())
    }

    private fun BitAccess.lowDefinition(
        viewport: Viewport,
        blocks: BytePacketBuilder
    ) {
        world.npcs.filterNotNull().forEach { // TODO iterate visible map regions to display all npcs when we do region support.
            if (viewport.localNPCs.contains(it)) return@forEach
            writeBits(15, it.index)
            writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
            var x = it.tile.x - viewport.player.tile.x
            var z = it.tile.z - viewport.player.tile.z
            if (extendedViewport) {
                if (x < 127) x += 256
                if (z < 127) z += 256
            } else {
                if (x < 15) x += 32
                if (z < 15) z += 32
            }
            writeBits(1, it.hasPendingUpdate().toInt())
            writeBits(3, 512) // TODO orientation
            writeBits(if (extendedViewport) 8 else 5, z)
            writeBits(1, 0) // TODO handle teleporting
            writeBits(14, it.id)
            writeBits(if (extendedViewport) 8 else 5, x)
            viewport.localNPCs.add(it)
            if (it.hasPendingUpdate()) encodePendingBlocks(it, blocks)
        }
    }

    private fun BitAccess.highDefinition(
        viewport: Viewport,
        blocks: BytePacketBuilder
    ) {
        viewport.localNPCs.forEach {
            if (!it.tile.withinDistance(viewport.player, if (extendedViewport) EXTENDED_VIEWPORT_DISTANCE else NORMAL_VIEWPORT_DISTANCE)) {
                removeNPC()
                return@forEach
            }
            val updating = processHighDefinitionNPC(it)
            if (updating) encodePendingBlocks(it, blocks)
        }
    }

    private fun BitAccess.processHighDefinitionNPC(npc: NPC): Boolean {
        val needsWalkUpdate = false
        val needsUpdate = npc.hasPendingUpdate()
        writeBits(1, needsUpdate.toInt())
        when {
            needsWalkUpdate -> {
                writeBits(2, 1) // TODO handle run direction
                writeBits(3, -1) // TODO handle walk direction
                // if run direction is not -1 send a bit of 3 with the next run direction
                writeBits(1, needsUpdate.toInt())
            }
            needsUpdate -> {
                writeBits(2, 0)
            }
        }
        return needsUpdate
    }

    private fun encodePendingBlocks(npc: NPC, blocks: BytePacketBuilder) {
        npc.pendingUpdates().map { it to renderingBlockMap[it::class]!! }.sortedWith(compareBy { it.second.index }).toMap().let { updates ->
            val mask = updates.map { it.value.mask }.sum().let { if (it >= 0xff) it or 0x4 else it }
            blocks.writeByte(mask.toByte())
            if (mask >= 0xff) { blocks.writeByte((mask shr 8).toByte()) }
            updates.forEach { blocks.writePacket(it.value.build(npc, it.key)) }
        }
    }

    private fun BitAccess.removeNPC() {
        writeBits(1, 1)
        writeBits(2, 3)
    }

    private companion object {
        const val EXTENDED_VIEWPORT_DISTANCE = 126
        const val NORMAL_VIEWPORT_DISTANCE = 14

        val renderingBlockMap = mapOf(
            Render.Sequence::class to NPCSequenceBlock(),
            Render.NPCCustomLevel::class to NPCCustomLevelBlock(),
            Render.FaceActor::class to NPCFaceActorBlock(),
            Render.FaceTile::class to NPCFaceTileBlock(),
            Render.ForceMovement::class to NPCForceMovementBlock(),
            Render.HitDamage::class to NPCHitDamageBlock(),
            Render.Recolor::class to NPCRecolorBlock(),
            Render.OverheadChat::class to NPCOverheadChatBlock(),
            Render.SpotAnimation::class to NPCSpotAnimationBlock(),
            Render.NPCTransmogrification::class to NPCTransmogrificationBlock()
        )
    }
}

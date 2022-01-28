package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.player.Viewport
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.withinDistance
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NpcInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.block.npc.FaceTileBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.HitDamageBlock
import com.runetopic.xlitekt.network.packet.assembler.block.npc.OverheadChatBlock
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.BitAccess
import com.runetopic.xlitekt.util.ext.toInt
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder

class NpcInfoPacketAssembler(
    private val extendedViewport: Boolean,
) : PacketAssembler<NpcInfoPacket>(opcode = if (extendedViewport) 90 else 78, size = -2) {

    override fun assemblePacket(packet: NpcInfoPacket) = buildPacket {
        val blocks = buildPacket {}

        withBitAccess {
            packet.player.viewport.let { viewport ->
                writeNPCSize(viewport)
                writeNPCs(viewport, blocks)
            }

            if (blocks.size > 0) {
                writeBits(15, Short.MAX_VALUE.toInt())
            }
        }

        writePacket(blocks.build())
    }

    private fun BitAccess.writeNPCs(viewport: Viewport, blocks: BytePacketBuilder) {
        viewport.localNPCs.forEach { npc ->
            val renderDistance = if (extendedViewport) EXTENDED_VIEWPORT_DISTANCE else NORMAL_VIEWPORT_DISTANCE
            if (!npc.tile.withinDistance(viewport.player, renderDistance)) {
                removeNPC()
                return@forEach
            }
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
            if (needsUpdate) writePendingUpdates(npc, blocks)
        }
        inject<World>().value.npcs.forEach { npc -> // TODO iterate visible map regions to display all npcs when we do region support.
            if (viewport.localNPCs.contains(npc)) return@forEach
            writeBits(15, npc.index)
            writeBits(1, 0) // if 1 == 1 read 32 bits they just don't use it atm. Looks like they're working on something
            var x = npc.tile.x - viewport.player.tile.x
            var z = npc.tile.z - viewport.player.tile.z
            if (extendedViewport) {
                if (x < 127) x += 256
                if (z < 127) z += 256
            } else {
                if (x < 15) x += 32
                if (z < 15) z += 32
            }
            writeBits(1, npc.hasPendingUpdate().toInt())
            writeBits(3, 512) // TODO orientation
            writeBits(if (extendedViewport) 8 else 5, z)
            writeBits(1, 0) // TODO handle teleporting
            writeBits(14, npc.id)
            writeBits(if (extendedViewport) 8 else 5, x)
            viewport.localNPCs.add(npc)
            if (npc.hasPendingUpdate()) writePendingUpdates(npc, blocks)
        }
    }

    private fun writePendingUpdates(npc: NPC, blocks: BytePacketBuilder) {
        val updates = npc.pendingUpdates().map { mapToBlock(it) }.sortedWith(compareBy { it.second.index }).toMap()
        var mask = 0x0
        updates.forEach { mask = mask or it.value.mask }
        if (mask >= 0xff) { mask = mask or 0x4 }
        blocks.writeByte(mask.toByte())
        if (mask >= 0xff) { blocks.writeByte((mask shr 8).toByte()) }
        updates.forEach { blocks.writePacket(it.value.build(npc, it.key)) }
    }

    private fun BitAccess.writeNPCSize(viewport: Viewport) = writeBits(8, viewport.localNPCs.size)

    private fun BitAccess.removeNPC() {
        writeBits(1, 1)
        writeBits(2, 3)
    }

    private fun mapToBlock(it: Render) = when (it) {
        is Render.OverheadChat -> it to OverheadChatBlock()
        is Render.FaceTile -> it to FaceTileBlock()
        is Render.HitDamage -> it to HitDamageBlock()
        else -> throw IllegalStateException("Unhandled npc block in NpcInfo. Block was $it")
    }

    private companion object {
        const val EXTENDED_VIEWPORT_DISTANCE = 126
        const val NORMAL_VIEWPORT_DISTANCE = 14
    }
}

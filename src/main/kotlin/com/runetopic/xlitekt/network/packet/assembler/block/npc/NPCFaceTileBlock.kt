package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeByteSubtract
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class NPCFaceTileBlock : RenderingBlock<NPC, Render.FaceTile>(1, 0x8) {
    override fun build(actor: NPC, render: Render.FaceTile) = buildPacket {
        writeShortLittleEndian(((render.location.x shl 1) + 1).toShort())
        writeShortLittleEndianAdd(((render.location.z shl 1) + 1).toShort())
        writeByteSubtract(0) // 1 == instant look = 0 is delayed look
    }
}

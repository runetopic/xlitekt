package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

class NPCFaceTileBlock : RenderingBlock<NPC, Render.FaceTile>(1, 0x8) {
    override fun build(actor: NPC, render: Render.FaceTile) = buildPacket {
        writeShortLittleEndian(((render.tile.x shl 1) + 1).toShort())
        writeShortLittleEndianAdd(((render.tile.z shl 1) + 1).toShort())
        writeByteSubtract(0) // 1 == instant look = 0 is delayed look
    }
}

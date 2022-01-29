package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

class NPCSequenceBlock : RenderingBlock<NPC, Render.Animation>(6, 0x40) {
    override fun build(actor: NPC, render: Render.Animation) = buildPacket {
        writeShortLittleEndianAdd(render.id.toShort())
        writeByteSubtract(render.delay.toByte())
    }
}

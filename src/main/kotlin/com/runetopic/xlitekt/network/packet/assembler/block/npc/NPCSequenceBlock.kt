package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class NPCSequenceBlock : RenderingBlock<NPC, Render.Sequence>(6, 0x40) {
    override fun build(actor: NPC, render: Render.Sequence) = buildPacket {
        writeShortLittleEndianAdd(render.id.toShort())
        writeByteSubtract(render.delay.toByte())
    }
}

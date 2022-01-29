package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class NPCTransmogrificationBlock : RenderingBlock<NPC, Render.NPCTransmogrification>(10, 0x20) {
    override fun build(actor: NPC, render: Render.NPCTransmogrification) = buildPacket {
        writeShortLittleEndian(render.id.toShort())
    }
}

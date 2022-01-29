package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

class NPCFaceActorBlock : RenderingBlock<NPC, Render.FaceActor>(8, 0x80) {
    override fun build(actor: NPC, render: Render.FaceActor) = buildPacket {
        writeShortLittleEndian(render.index.toShort())
    }
}

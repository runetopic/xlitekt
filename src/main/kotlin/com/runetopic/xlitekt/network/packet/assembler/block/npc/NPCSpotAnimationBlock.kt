package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeIntV2
import com.runetopic.xlitekt.util.ext.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class NPCSpotAnimationBlock : RenderingBlock<NPC, Render.SpotAnimation>(4, 0x2) {
    override fun build(actor: NPC, render: Render.SpotAnimation) = buildPacket {
        writeShortLittleEndianAdd(render.id.toShort())
        writeIntV2(render.packedMetaData())
    }
}

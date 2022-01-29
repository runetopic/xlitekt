package com.runetopic.xlitekt.network.packet.assembler.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.network.packet.assembler.block.RenderingBlock
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class NPCOverheadChatBlock : RenderingBlock<NPC, Render.OverheadChat>(3, 0x10) {
    override fun build(actor: NPC, render: Render.OverheadChat) = buildPacket {
        writeStringCp1252NullTerminated(render.text)
    }
}

package com.runetopic.xlitekt.game.actor.render.block.npc

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.actor.render.block.RenderingBlock
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class NPCOverheadChatBlock : RenderingBlock<NPC, Render.OverheadChat>(3, 0x10) {
    override fun build(actor: NPC, render: Render.OverheadChat) = buildPacket {
        writeStringCp1252NullTerminated(render.text)
    }
}

package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeStringCp1252NullTerminated

/**
 * @author Tyler Telis
 */
class NPCOverheadChatBlock : RenderingBlock<NPC, Render.OverheadChat>(3, 0x10) {
    override fun build(actor: NPC, render: Render.OverheadChat) = buildPacket {
        writeStringCp1252NullTerminated(render.text)
    }
}

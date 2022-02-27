package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeIntV1

/**
 * @author Tyler Telis
 */
class NPCCustomLevelBlock : RenderingBlock<NPC, Render.NPCCustomLevel>(5, 0x200) {
    override fun build(actor: NPC, render: Render.NPCCustomLevel) = buildPacket {
        writeIntV1(render.level)
    }
}

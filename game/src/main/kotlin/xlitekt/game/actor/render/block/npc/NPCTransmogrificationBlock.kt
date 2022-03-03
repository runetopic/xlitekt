package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock

/**
 * @author Tyler Telis
 */
class NPCTransmogrificationBlock : RenderingBlock<NPC, Render.NPCTransmogrification>(10, 0x20) {
    override fun build(actor: NPC, render: Render.NPCTransmogrification) = buildPacket {
        writeShortLittleEndian(render.id.toShort())
    }
}

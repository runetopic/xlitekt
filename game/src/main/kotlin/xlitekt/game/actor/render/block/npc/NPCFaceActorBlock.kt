package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock

/**
 * @author Tyler Telis
 */
class NPCFaceActorBlock : RenderingBlock<NPC, Render.FaceActor>(8, 0x80) {
    override fun build(actor: NPC, render: Render.FaceActor) = buildPacket {
        writeShortLittleEndian(render.index.toShort())
    }
}

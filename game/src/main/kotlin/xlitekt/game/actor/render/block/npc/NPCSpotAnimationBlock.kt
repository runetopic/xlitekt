package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class NPCSpotAnimationBlock : RenderingBlock<NPC, Render.SpotAnimation>(4, 0x2) {
    override fun build(actor: NPC, render: Render.SpotAnimation) = buildPacket {
        writeShortLittleEndianAdd(render.id.toShort())
        writeIntV2(render.packedMetaData())
    }
}

package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class NPCSequenceBlock : RenderingBlock<NPC, Render.Sequence>(6, 0x40) {
    override fun build(actor: NPC, render: Render.Sequence) = buildPacket {
        writeShortLittleEndianAdd(render.id.toShort())
        writeByteSubtract(render.delay.toByte())
    }
}

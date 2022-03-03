package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteNegate

/**
 * @author Tyler Telis
 */
class NPCRecolorBlock : RenderingBlock<NPC, Render.Recolor>(9, 0x100) {
    override fun build(actor: NPC, render: Render.Recolor) = buildPacket {
        writeShort(render.startDelay.toShort())
        writeShortLittleEndian(render.endDelay.toShort())
        writeByte(render.hue.toByte())
        writeByteNegate(render.saturation.toByte())
        writeByteNegate(render.luminance.toByte())
        writeByte(render.amount.toByte())
    }
}

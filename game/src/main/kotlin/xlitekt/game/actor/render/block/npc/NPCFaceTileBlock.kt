package xlitekt.game.actor.render.block.npc

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.RenderingBlock
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Tyler Telis
 */
class NPCFaceTileBlock : RenderingBlock<NPC, Render.FaceTile>(1, 0x8) {
    override fun build(actor: NPC, render: Render.FaceTile) = buildPacket {
        writeShortLittleEndian(((render.location.x shl 1) + 1).toShort())
        writeShortLittleEndianAdd(((render.location.z shl 1) + 1).toShort())
        writeByteSubtract(0) // 1 == instant look = 0 is delayed look
    }
}

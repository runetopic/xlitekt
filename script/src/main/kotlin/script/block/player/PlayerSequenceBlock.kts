package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Sequence>(8, 0x2) {
    buildPacket {
        writeShortAdd(id.toShort())
        writeByteNegate(delay.toByte())
    }
}

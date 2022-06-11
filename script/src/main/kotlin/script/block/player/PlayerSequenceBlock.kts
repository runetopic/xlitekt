package script.block.player

import xlitekt.game.actor.render.Render.Sequence
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<Sequence>(8, 0x2) {
    buildFixedPacket(3) {
        writeShortAdd(id)
        writeByteNegate(delay)
    }
}

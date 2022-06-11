package script.block.player

import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<TemporaryMovementType>(2, 0x2000) {
    buildFixedPacket(1) {
        writeByte(id)
    }
}

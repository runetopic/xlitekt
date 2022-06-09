package script.block.player

import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.allocate
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<TemporaryMovementType>(2, 0x2000) {
    allocate(1) {
        writeByte { id }
    }
}

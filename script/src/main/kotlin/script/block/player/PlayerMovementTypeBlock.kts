package script.block.player

import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.block.fixedPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
fixedPlayerUpdateBlock<MovementType>(index = 9, mask = 0x400, size = 1) {
    it.writeByteAdd(running.toInt() + 1)
}

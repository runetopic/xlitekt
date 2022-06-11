package script.block.player

import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.toInt

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<MovementType>(9, 0x400) {
    buildFixedPacket(1) {
        writeByteAdd(running.toInt() + 1)
    }
}

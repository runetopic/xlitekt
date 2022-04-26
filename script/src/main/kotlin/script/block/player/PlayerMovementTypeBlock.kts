package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.MovementType
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.toByte

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<MovementType>(9, 0x400) {
    buildPacket {
        writeByteAdd { running.toByte() + 1 }
    }
}

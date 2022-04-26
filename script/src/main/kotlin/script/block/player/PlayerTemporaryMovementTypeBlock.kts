package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.TemporaryMovementType
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeByte

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<TemporaryMovementType>(2, 0x2000) {
    buildPacket {
        writeByte { id }
    }
}

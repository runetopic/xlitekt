package script.block.player

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.actor.render.Render.FaceDirection
import xlitekt.game.actor.render.block.onPlayerUpdateBlock

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceDirection>(12, 0x40) {
    buildPacket {
        writeShortLittleEndian(direction.toShort())
    }
}

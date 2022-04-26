package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.FaceAngle
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<FaceAngle>(12, 0x40) {
    buildPacket {
        writeShortLittleEndian { angle }
    }
}

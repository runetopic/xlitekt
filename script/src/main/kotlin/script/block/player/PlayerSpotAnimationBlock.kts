package script.block.player

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<SpotAnimation>(10, 0x800) {
    buildPacket {
        writeShortAdd(id.toShort())
        writeIntV2(packedMetaData())
    }
}

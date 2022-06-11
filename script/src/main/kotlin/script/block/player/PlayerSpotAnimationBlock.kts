package script.block.player

import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.onPlayerUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortAdd

/**
 * @author Jordan Abraham
 */
onPlayerUpdateBlock<SpotAnimation>(10, 0x800) {
    buildFixedPacket(6) {
        writeShortAdd(id)
        writeIntV2(packedMetaData())
    }
}

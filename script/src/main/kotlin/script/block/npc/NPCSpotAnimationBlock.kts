package script.block.npc

import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<SpotAnimation>(4, 0x2) {
    buildFixedPacket(6) {
        writeShortLittleEndianAdd(id)
        writeIntV2(packedMetaData())
    }
}

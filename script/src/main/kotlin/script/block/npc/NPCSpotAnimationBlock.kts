package script.block.npc

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.actor.render.Render.SpotAnimation
import xlitekt.game.actor.render.block.onNPCUpdateBlock
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onNPCUpdateBlock<SpotAnimation>(4, 0x2) {
    buildPacket {
        writeShortLittleEndianAdd(id.toShort())
        writeIntV2(packedMetaData())
    }
}

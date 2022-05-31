package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LocAddPacket>(opcode = 18, size = 4) {
    buildPacket {
        writeByteSubtract { (shape shl 2) or (rotation and 0x3) }
        writeByteSubtract { packedOffset }
        writeShortLittleEndianAdd { id }
    }
}

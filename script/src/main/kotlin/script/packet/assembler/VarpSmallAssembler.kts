package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpSmallPacket>(opcode = 94, size = 3) {
    buildPacket {
        writeByteAdd { value }
        writeShort { id }
    }
}

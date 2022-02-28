package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpSmallPacket>(opcode = 94, size = 3) {
    buildPacket {
        writeByteAdd(value.toByte())
        writeShort(id.toShort())
    }
}

package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.packet.IfOpenSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.toByte

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenSubPacket>(opcode = 2, size = 7) {
    buildPacket {
        writeShortLittleEndian(interfaceId.toShort())
        writeByteSubtract(alwaysOpen.toByte())
        writeInt(toPackedInterface)
    }
}

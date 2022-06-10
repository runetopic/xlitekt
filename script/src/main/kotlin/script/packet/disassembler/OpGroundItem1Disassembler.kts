import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.OpGroundItemPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readByte
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 76, size = 7) {
    OpGroundItemPacket(
        index = 1,
        x = readUShortLittleEndianAdd(),
        itemId = readUShort(),
        isModified = readByte().toBoolean(),
        z = readUShortAdd()
    )
}

import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.OpGroundItemPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteAdd
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 21, size = 7) {
    OpGroundItemPacket(
        index = 2,
        z = readUShortAdd(),
        isModified = readUByteAdd().toBoolean(),
        itemId = readUShort().toInt(),
        x = readUShortLittleEndian()
    )
}

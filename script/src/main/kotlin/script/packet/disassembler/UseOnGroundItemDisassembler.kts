import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.UseOnGroundItemPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.buffer.readIntV1
import xlitekt.shared.buffer.readUByteAdd
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 39, size = 15) {
    val packet = UseOnGroundItemPacket(
        itemId = readUShort(),
        slotId = readUShort(),
        z = readUShort(),
        groundItemId = readUShortLittleEndianAdd(),
        packedInterface = readIntV1(),
        isModified = readUByteAdd().toBoolean(),
        x = readUShortLittleEndianAdd()
    )

    logger.debug { packet }

    packet
}

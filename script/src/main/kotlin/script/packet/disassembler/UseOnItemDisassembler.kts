import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.UseOnInventoryItemPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readIntV2
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 2, size = 16) {
    val packet = UseOnInventoryItemPacket(
        toSlotId = readUShortAdd(),
        fromItemId = readUShort().toInt(),
        toItemId = readUShortAdd(),
        fromPackedInterface = readInt(), // TODO which interface is to/from?
        toPackedInterface = readIntV2(),
        fromSlotId = readUShortLittleEndian()
    )

    logger.debug { packet }

    packet
}

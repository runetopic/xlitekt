import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readIntLittleEndian
import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.UseOnLocPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByteNegate
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 28, size = 15) {

    val isModified = readUByteNegate().toBoolean()
    val locId = readUShort().toInt()
    val itemId = readUShort().toInt()
    val z = readUShortLittleEndian()
    val packedInterface = readIntLittleEndian()
    val slotId = readUShortAdd()
    val x = readUShortAdd()

    val packet = UseOnLocPacket(
        isModified = isModified,
        itemId = itemId,
        locId = locId,
        x = x,
        z = z,
        slotId = slotId,
        packedInterface = packedInterface
    )

    logger.debug { packet }

    packet
}
package script.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readInt
import xlitekt.game.packet.UseOnNPCPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 38, size = 11) {
    val packet = UseOnNPCPacket(
        itemId = readUShortLittleEndian(),
        isModified = (128 - readByte()).toBoolean(),
        npcIndex = readUShortLittleEndian(),
        interfaceId = readInt(),
        slotId = readUShortLittleEndian(),
    )

    logger.debug { packet }

    packet
}

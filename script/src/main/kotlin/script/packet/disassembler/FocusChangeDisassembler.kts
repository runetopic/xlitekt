import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.FocusChangePacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
private val logger = InlineLogger()

onPacketDisassembler(opcode = 7, size = 1) {

    val isFocused = readByte().toInt().toBoolean()

    val packet = FocusChangePacket(isFocused)

    logger.debug { packet }

    packet
}

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.FocusChangePacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.toBoolean

/**
 * @author Justin Kenney
 */
private val logger = InlineLogger()

onPacketDisassembler(opcode = 7, size = 1) {

    val packet = FocusChangePacket(readByte().toInt().toBoolean())

    logger.debug { packet }

    packet
}

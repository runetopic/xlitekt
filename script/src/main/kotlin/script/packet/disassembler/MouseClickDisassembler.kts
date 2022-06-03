package script.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.MouseClickPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 35, size = 6) {
    val packet = MouseClickPacket(
        time = readUShort().toLong(),
        x = readUShort().toInt(),
        y = readUShort().toInt()
    )

//    logger.debug { packet }

    packet
}

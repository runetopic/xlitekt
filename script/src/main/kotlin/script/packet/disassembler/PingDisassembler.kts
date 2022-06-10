package script.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.packet.PingPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

val logger = InlineLogger()

onPacketDisassembler(opcode = 12, size = 0) {

//    logger.debug { "Ping Received" }

    PingPacket()
}

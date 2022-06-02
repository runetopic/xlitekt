package script.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.readShort
import xlitekt.game.packet.MouseClickPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Justin Kenney
 */
val logger = InlineLogger()

onPacketDisassembler(opcode = 35, size = 6) {

    val lastTime = readShort().toLong()
    val x = readShort().toInt()
    val y = readShort().toInt()

    logger.debug { "Mouse Clicked: lastTime=$lastTime position=($x, $y)" }

    MouseClickPacket(lastTime, x, y)
}

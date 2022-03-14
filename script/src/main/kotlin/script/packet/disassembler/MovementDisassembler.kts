package script.packet.disassembler

import io.ktor.utils.io.core.readUByte
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readShortAdd
import xlitekt.shared.buffer.readShortLittleEndianAdd

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcodes = intArrayOf(57, 58), size = -1) {
    MovementPacket(
        movementType = readUByte().toInt(),
        destinationZ = readShortLittleEndianAdd(),
        destinationX = readShortAdd()
    )
}

package script.packet.disassembler

import io.ktor.utils.io.core.readUByte
import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndianAdd

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 57, size = -1) {
    MovementPacket(
        movementType = readUByte().toInt(),
        destinationX = readUShortLittleEndianAdd(),
        destinationZ = readUShortAdd()
    )
}

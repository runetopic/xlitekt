package script.packet.disassembler

import xlitekt.game.packet.MovementPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndianAdd

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcodes = intArrayOf(57, 58), size = -1) {
    MovementPacket(
        movementType = readUByte(),
        destinationZ = readUShortLittleEndianAdd(),
        destinationX = readUShortAdd()
    )
}

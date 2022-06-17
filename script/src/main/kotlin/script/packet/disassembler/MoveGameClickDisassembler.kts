package script.packet.disassembler

import xlitekt.game.packet.MoveGameClickPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketDisassemblerListener>().disassemblePacket(opcode = 57, size = -1) {
    MoveGameClickPacket(
        movementType = readUByte(),
        destinationZ = readUShortLittleEndianAdd(),
        destinationX = readUShortAdd()
    )
}

package script.packet.disassembler

import xlitekt.game.packet.MoveMinimapClickPacket
import xlitekt.game.packet.disassembler.PacketDisassemblerListener
import xlitekt.shared.buffer.readUByte
import xlitekt.shared.buffer.readUShort
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndianAdd
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketDisassemblerListener>().disassemblePacket(opcode = 58, size = -1) {
    MoveMinimapClickPacket(
        movementType = readUByte(),
        destinationZ = readUShortLittleEndianAdd(),
        destinationX = readUShortAdd(),
        mouseClickedX = readUByte(),
        mouseClickedZ = readUByte(),
        cameraAngleZ = readUShort(),
        value1 = readUByte(),
        value2 = readUByte(),
        value3 = readUByte(),
        value4 = readUByte(),
        currentX = readUShort(),
        currentZ = readUShort(),
        value5 = readUByte()
    )
}

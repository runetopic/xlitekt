package script.packet.assembler

import xlitekt.game.packet.MapProjAnimPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.buffer.writeShortLittleEndianAdd

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MapProjAnimPacket>(opcode = 64, size = 15) {
    it.writeByteAdd(packedOffset) // packedOffset
    it.writeShortLittleEndian(-1) // targetIndex
    it.writeByteNegate(distanceX) // ?
    it.writeShort(lifespan) // endCycle
    it.writeByte(startHeight) // startHeight
    it.writeShortLittleEndian(id)
    it.writeByte(steepness) // ?
    it.writeByteNegate(angle) // slope
    it.writeShortLittleEndianAdd(delay) // startCycle
    it.writeByte(endHeight) // endHeight
    it.writeByteAdd(distanceZ) // ?
}

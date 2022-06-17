package script.packet.assembler

import xlitekt.game.packet.LocAddPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShortLittleEndianAdd
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<LocAddPacket>(opcode = 18, size = 4) {
    it.writeByteSubtract((shape shl 2) or (rotation and 0x3))
    it.writeByteSubtract(packedOffset)
    it.writeShortLittleEndianAdd(id)
}

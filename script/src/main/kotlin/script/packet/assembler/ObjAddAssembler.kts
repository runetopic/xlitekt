package script.packet.assembler

import xlitekt.game.packet.ObjAddPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<ObjAddPacket>(opcode = 61, size = 5) {
    it.writeShort(id)
    it.writeByteSubtract(packedOffset)
    it.writeShortLittleEndian(amount)
}

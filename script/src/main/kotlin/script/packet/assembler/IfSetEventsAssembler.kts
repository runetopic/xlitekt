package script.packet.assembler

import xlitekt.game.packet.IfSetEventsPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<IfSetEventsPacket>(opcode = 76, size = 12) {
    it.writeIntV2(event)
    it.writeShortLittleEndian(fromSlot)
    it.writeShortLittleEndian(toSlot)
    it.writeInt(packedInterface)
}

package script.packet.assembler

import xlitekt.game.packet.IfSetEventsPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildFixedPacket
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeIntV2
import xlitekt.shared.buffer.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetEventsPacket>(opcode = 76, size = 12) {
    buildFixedPacket(12) {
        writeIntV2(event)
        writeShortLittleEndian(fromSlot)
        writeShortLittleEndian(toSlot)
        writeInt(packedInterface)
    }
}

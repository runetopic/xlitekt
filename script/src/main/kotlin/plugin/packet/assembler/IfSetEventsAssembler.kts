package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.packet.IfSetEventsPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeIntV2

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetEventsPacket>(opcode = 76, size = 12) {
    buildPacket {
        writeIntV2(event)
        writeShortLittleEndian(fromSlot.toShort())
        writeShortLittleEndian(toSlot.toShort())
        writeInt(packedInterface)
    }
}

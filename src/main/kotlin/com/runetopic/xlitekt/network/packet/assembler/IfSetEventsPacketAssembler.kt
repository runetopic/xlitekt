package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.util.ext.packInterface
import com.runetopic.xlitekt.util.ext.writeIntV2
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class IfSetEventsPacketAssembler : PacketAssembler<IfSetEventsPacket>(opcode = 76, size = 12) {
    override fun assemblePacket(packet: IfSetEventsPacket) = buildPacket {
        writeIntV2(packet.events)
        writeShortLittleEndian(packet.fromSlot.toShort())
        writeShortLittleEndian(packet.toSlot.toShort())
        writeInt(packet.interfaceId.packInterface(packet.childId))
    }
}

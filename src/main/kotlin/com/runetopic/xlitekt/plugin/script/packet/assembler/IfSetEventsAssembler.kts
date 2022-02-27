package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeIntV2
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian

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

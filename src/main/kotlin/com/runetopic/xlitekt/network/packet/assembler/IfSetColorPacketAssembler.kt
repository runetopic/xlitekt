package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetColorPacket
import com.runetopic.xlitekt.util.ext.writeIntV2
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class IfSetColorPacketAssembler : PacketAssembler<IfSetColorPacket>(opcode = 62, size = 6) {
    override fun assemblePacket(packet: IfSetColorPacket) = buildPacket {
        writeIntV2(packet.packedInterface)
        writeShortLittleEndian(packet.color.toShort())
    }
}

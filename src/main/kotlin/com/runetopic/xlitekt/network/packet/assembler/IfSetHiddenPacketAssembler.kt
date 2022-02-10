package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetHiddenPacket
import com.runetopic.xlitekt.util.ext.toByte
import com.runetopic.xlitekt.util.ext.writeIntV1
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class IfSetHiddenPacketAssembler : PacketAssembler<IfSetHiddenPacket>(opcode = 57, size = 5) {
    override fun assemblePacket(packet: IfSetHiddenPacket) = buildPacket {
        writeByte(packet.hidden.toByte())
        writeIntV1(packet.packedInterface)
    }
}

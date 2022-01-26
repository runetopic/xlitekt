package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import io.ktor.utils.io.core.writeShort

class IfOpenTopPacketAssembler : PacketAssembler<IfOpenTopPacket>(opcode = 31, size = 2) {
    override fun assemblePacket(packet: IfOpenTopPacket) = buildPacket {
        writeShort(packet.interfaceId.toShort())
    }
}

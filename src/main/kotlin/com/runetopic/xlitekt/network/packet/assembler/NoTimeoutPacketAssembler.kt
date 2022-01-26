package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.NoTimeoutPacket

class NoTimeoutPacketAssembler : PacketAssembler<NoTimeoutPacket>(opcode = 58, size = 0) {
    override fun assemblePacket(packet: NoTimeoutPacket) = buildPacket {}
}

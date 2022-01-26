package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.LogoutPacket

class LogoutPacketAssembler : PacketAssembler<LogoutPacket>(opcode = 87, size = 0) {
    override fun assemblePacket(message: LogoutPacket) = buildPacket {}
}

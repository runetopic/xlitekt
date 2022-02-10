package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.LogoutPacket
import io.ktor.utils.io.core.buildPacket

class LogoutPacketAssembler : PacketAssembler<LogoutPacket>(opcode = 20, size = 1) {
    override fun assemblePacket(packet: LogoutPacket) = buildPacket {
        writeByte(packet.type.toByte())
    }
}

package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.NoTimeoutPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
class NoTimeoutPacketAssembler : PacketAssembler<NoTimeoutPacket>(opcode = 58, size = 0) {
    override fun assemblePacket(packet: NoTimeoutPacket) = buildPacket {}
}

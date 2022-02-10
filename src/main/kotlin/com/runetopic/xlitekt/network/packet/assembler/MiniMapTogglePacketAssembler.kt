package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.MiniMapTogglePacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class MiniMapTogglePacketAssembler : PacketAssembler<MiniMapTogglePacket>(opcode = 36, size = 1) {
    override fun assemblePacket(packet: MiniMapTogglePacket) = buildPacket {
        writeByte(packet.type.toByte())
    }
}

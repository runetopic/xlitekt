package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdatePrivateChatStatusPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class UpdatePrivateChatStatusPacketAssembler : PacketAssembler<UpdatePrivateChatStatusPacket>(opcode = 85, size = 1) {
    override fun assemblePacket(packet: UpdatePrivateChatStatusPacket) = buildPacket {
        writeByte(packet.mode.toByte())
    }
}

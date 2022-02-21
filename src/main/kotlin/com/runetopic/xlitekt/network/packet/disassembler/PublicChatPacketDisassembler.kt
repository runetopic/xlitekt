package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.PublicChatPacket
import com.runetopic.xlitekt.shared.buffer.readUShortSmart
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readUByte

class PublicChatPacketDisassembler : PacketDisassembler<PublicChatPacket>(opcode = 95, size = -1) {
    override fun disassemblePacket(packet: ByteReadPacket) = PublicChatPacket(
        unknown = packet.readUByte().toInt(),
        color = packet.readUByte().toInt(),
        effect = packet.readUByte().toInt(),
        length = packet.readUShortSmart(),
        data = packet.readBytes()
    )
}

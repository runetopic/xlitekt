package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.PublicChatPacket
import com.runetopic.xlitekt.shared.buffer.readSmart
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readUByte

class PublicChatPacketDisassembler : PacketDisassembler<PublicChatPacket>(opcode = 95, size = -1) {
    override fun disassemblePacket(packet: ByteReadPacket): PublicChatPacket {
        packet.readUByte()
        val color = packet.readUByte().toInt()
        val effect = packet.readUByte().toInt()
        val length = packet.readSmart()
        val data = packet.readBytes()
        return PublicChatPacket(color, effect, length, data)
    }
}

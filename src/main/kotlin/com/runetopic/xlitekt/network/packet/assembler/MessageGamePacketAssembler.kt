package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.shared.buffer.writeSmart
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class MessageGamePacketAssembler : PacketAssembler<MessageGamePacket>(opcode = 69, size = -1) {
    override fun assemblePacket(packet: MessageGamePacket) = buildPacket {
        writeSmart(packet.type)
        writeByte(packet.hasPrefix.toByte())
        if (packet.hasPrefix) writeStringCp1252NullTerminated(packet.prefix)
        writeStringCp1252NullTerminated(packet.message)
    }
}

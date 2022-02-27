package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.PublicChatPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import com.runetopic.xlitekt.shared.buffer.readUShortSmart
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.readUByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 95, size = -1) {
    PublicChatPacket(
        unknown = readUByte().toInt(),
        color = readUByte().toInt(),
        effect = readUByte().toInt(),
        length = readUShortSmart(),
        data = readBytes()
    )
}

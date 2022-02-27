package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.WindowStatusPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 48, size = 5) {
    WindowStatusPacket(
        displayMode = readByte().toInt(),
        width = readUShort().toInt(),
        height = readUShort().toInt()
    )
}

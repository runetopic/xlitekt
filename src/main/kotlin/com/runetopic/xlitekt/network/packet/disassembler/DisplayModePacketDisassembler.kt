package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.DisplayModePacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUShort

/**
 * @author Tyler Telis
 */
class DisplayModePacketDisassembler : PacketDisassembler<DisplayModePacket>(opcode = 48, size = 5) {
    override fun disassemblePacket(packet: ByteReadPacket): DisplayModePacket {
        val status = packet.readByte().toInt().shr(1)
        val width = packet.readUShort().toInt()
        val height = packet.readUShort().toInt()
        return DisplayModePacket(status, width, height)
    }
}

package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.WindowStatusPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUShort

/**
 * @author Tyler Telis
 */
class WindowStatusPacketDisassembler : PacketDisassembler<WindowStatusPacket>(opcode = 48, size = 5) {
    override fun disassemblePacket(packet: ByteReadPacket) = WindowStatusPacket(
        displayMode = packet.readByte().toInt(),
        width = packet.readUShort().toInt(),
        height = packet.readUShort().toInt()
    )
}

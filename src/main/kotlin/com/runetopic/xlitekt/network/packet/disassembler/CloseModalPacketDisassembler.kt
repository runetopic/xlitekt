package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.CloseModalPacket
import io.ktor.utils.io.core.ByteReadPacket

class CloseModalPacketDisassembler : PacketDisassembler<CloseModalPacket>(opcode = 96, 0) {
    override fun disassemblePacket(packet: ByteReadPacket): CloseModalPacket = CloseModalPacket()
}

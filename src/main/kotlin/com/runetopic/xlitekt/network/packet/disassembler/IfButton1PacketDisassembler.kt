package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket

class IfButton1PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = -1, size = 0) {
    override fun disassemblePacket(reader: ByteReadPacket): IfButtonPacket = IfButtonPacket(1)
}

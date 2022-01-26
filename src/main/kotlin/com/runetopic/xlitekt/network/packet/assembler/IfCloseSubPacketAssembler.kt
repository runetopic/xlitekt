package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import io.ktor.utils.io.core.writeInt

class IfCloseSubPacketAssembler : PacketAssembler<IfCloseSubPacket>(opcode = 13, size = 4) {
    override fun assemblePacket(packet: IfCloseSubPacket) = buildPacket {
        writeInt(packet.hash)
    }
}

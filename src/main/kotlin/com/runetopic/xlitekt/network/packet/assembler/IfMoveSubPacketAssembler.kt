package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import io.ktor.utils.io.core.writeInt

class IfMoveSubPacketAssembler : PacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    override fun assemblePacket(message: IfMoveSubPacket) = buildPacket {
        writeInt(message.fromHash)
        writeInt(message.toHash)
    }
}

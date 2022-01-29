package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import io.ktor.utils.io.core.writeInt

/**
 * @author Tyler Telis
 */
class IfMoveSubPacketAssembler : PacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    override fun assemblePacket(packet: IfMoveSubPacket) = buildPacket {
        writeInt(packet.fromPackedInterface)
        writeInt(packet.toPackedInterface)
    }
}

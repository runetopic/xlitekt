package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.SetMapFlagPacket

class SetMapFlagPacketAssembler : PacketAssembler<SetMapFlagPacket>(opcode = 93, size = 2) {
    override fun assemblePacket(message: SetMapFlagPacket) = buildPacket {
        writeByte(message.destinationX.toByte())
        writeByte(message.destinationZ.toByte())
    }
}

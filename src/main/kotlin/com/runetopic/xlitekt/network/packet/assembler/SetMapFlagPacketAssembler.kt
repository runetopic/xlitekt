package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.SetMapFlagPacket

class SetMapFlagPacketAssembler : PacketAssembler<SetMapFlagPacket>(opcode = 93, size = 2) {
    override fun assemblePacket(packet: SetMapFlagPacket) = buildPacket {
        writeByte(packet.destinationX.toByte())
        writeByte(packet.destinationZ.toByte())
    }
}

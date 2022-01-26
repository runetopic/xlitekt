package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateWeightPacket
import io.ktor.utils.io.core.writeShort

class UpdateWeightPacketAssembler : PacketAssembler<UpdateWeightPacket>(opcode = 50, size = -2) {
    override fun assemblePacket(packet: UpdateWeightPacket) = buildPacket {
        writeShort(packet.weight)
    }
}

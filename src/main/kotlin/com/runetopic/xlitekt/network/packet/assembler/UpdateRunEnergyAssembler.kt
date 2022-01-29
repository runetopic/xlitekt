package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateRunEnergyPacket

/**
 * @author Tyler Telis
 */
class UpdateRunEnergyAssembler : PacketAssembler<UpdateRunEnergyPacket>(opcode = 59, size = -1) {
    override fun assemblePacket(packet: UpdateRunEnergyPacket) = buildPacket {
        writeByte((packet.energy / 100).toInt().toByte())
    }
}

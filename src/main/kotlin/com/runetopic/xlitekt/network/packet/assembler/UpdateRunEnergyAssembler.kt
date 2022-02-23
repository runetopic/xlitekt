package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateRunEnergyPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class UpdateRunEnergyAssembler : PacketAssembler<UpdateRunEnergyPacket>(opcode = 59, size = 1) {
    override fun assemblePacket(packet: UpdateRunEnergyPacket) = buildPacket {
        writeByte((packet.energy).toInt().toByte())
    }
}

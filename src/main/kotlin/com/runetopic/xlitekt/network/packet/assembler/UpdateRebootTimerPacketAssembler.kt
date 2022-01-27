package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateRebootTimerPacket
import io.ktor.utils.io.core.writeShort

class UpdateRebootTimerPacketAssembler : PacketAssembler<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    override fun assemblePacket(packet: UpdateRebootTimerPacket) = buildPacket {
        writeShort(packet.rebootTimer.toShort())
    }
}

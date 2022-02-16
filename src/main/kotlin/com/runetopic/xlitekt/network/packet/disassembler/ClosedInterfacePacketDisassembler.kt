package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.ClosedInterfacePacket
import io.ktor.utils.io.core.ByteReadPacket

class ClosedInterfacePacketDisassembler : PacketDisassembler<ClosedInterfacePacket>(opcode = 96, 0) {
    override fun disassemblePacket(packet: ByteReadPacket): ClosedInterfacePacket = ClosedInterfacePacket()
}

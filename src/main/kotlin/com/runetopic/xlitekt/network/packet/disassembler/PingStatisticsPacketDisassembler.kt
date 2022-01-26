package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.PingStatisticsPacket
import io.ktor.utils.io.core.ByteReadPacket

class PingStatisticsPacketDisassembler : PacketDisassembler<PingStatisticsPacket>(opcode = 12, size = 0) {
    override fun disassemblePacket(packet: ByteReadPacket): PingStatisticsPacket = PingStatisticsPacket()
}

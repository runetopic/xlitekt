package com.runetopic.xlitekt.network.packet.read

import com.runetopic.xlitekt.network.packet.Packet

class PingStatisticsPacket : Packet {
    override fun opcode(): Int = 12
    override fun size(): Int = 0
    override fun builder() = buildPacket {}
}

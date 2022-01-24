package com.runetopic.xlitekt.network.packet.write

import com.runetopic.xlitekt.network.packet.Packet

class NoTimeoutPacket : Packet {
    override fun opcode(): Int = 58
    override fun size(): Int = 0
    override fun builder() = buildPacket {}
}

package com.runetopic.xlitekt.network.packet.write

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.writeShort

class IfOpenTopPacket(
    private val interfaceId: Int
) : Packet {
    override fun opcode(): Int = 31
    override fun size(): Int = 2

    override fun builder() = buildPacket {
        writeShort(interfaceId.toShort())
    }
}

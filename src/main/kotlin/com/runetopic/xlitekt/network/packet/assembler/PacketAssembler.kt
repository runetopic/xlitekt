package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
data class PacketAssembler(
    val opcode: Int,
    val size: Int,
    val packet: Packet.() -> ByteReadPacket
)

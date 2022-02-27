package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
data class PacketDisassembler(
    val opcode: Int,
    val size: Int,
    val packet: ByteReadPacket.() -> Packet
)

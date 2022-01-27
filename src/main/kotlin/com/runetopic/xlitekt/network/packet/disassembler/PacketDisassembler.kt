package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
abstract class PacketDisassembler<out P : Packet>(val opcode: Int, val size: Int) {
    abstract fun disassemblePacket(packet: ByteReadPacket): @UnsafeVariance P
}

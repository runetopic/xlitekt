package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
abstract class PacketAssembler<out P : Packet>(val opcode: Int, val size: Int) {
    abstract fun assemblePacket(packet: @UnsafeVariance P): ByteReadPacket
}

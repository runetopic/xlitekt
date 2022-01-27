package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.BytePacketBuilder

/**
 * @author Jordan Abraham
 */
abstract class PacketAssembler<out P : Packet>(val opcode: Int, val size: Int) {
    abstract fun assemblePacket(packet: @UnsafeVariance P): BytePacketBuilder

    inline fun buildPacket(block: BytePacketBuilder.() -> Unit): BytePacketBuilder {
        val builder = BytePacketBuilder()
        block.invoke(builder)
        return builder
    }
}

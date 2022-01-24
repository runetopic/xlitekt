package com.runetopic.xlitekt.network.packet

import io.ktor.utils.io.core.BytePacketBuilder

interface Packet {
    fun opcode(): Int
    fun size(): Int
    fun builder(): BytePacketBuilder

    fun buildPacket(block: BytePacketBuilder.() -> Unit): BytePacketBuilder {
        val builder = BytePacketBuilder()
        block.invoke(builder)
        return builder
    }

    companion object {
        val READ_SIZES = intArrayOf(
            3, 8, 16, -1, -1, 8, 2, 1, 8, 0, 8, 8, 0, 2, 16, 16, 8, 9, 11, 8, 8, 7, 2, 4, 7, 4, -1, 7, 15, 8, -1, 8, 9,
            7, 15, 6, -1, 3, 11, 15, 4, 8, 8, 8, -1, -2, 3, 3, 5, -1, -1, -1, -1, 7, 7, 0, 7, -1, -1, 3, -2, 3, 8, 3, 8,
            0, 8, 4, 7, 11, 3, 6, 8, 11, 3, 8, 7, 4, -1, 2, 15, 7, 8, 3, 14, 3, -1, 10, 8, 13, 3, -1, 3, -1, 7, -1, 0,
            -2, -1, -1, 2, -1, -1, -1, 3, -1
        )
    }
}

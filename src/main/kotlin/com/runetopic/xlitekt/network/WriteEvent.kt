package com.runetopic.xlitekt.network

import java.nio.ByteBuffer

sealed class WriteEvent {
    data class HandshakeWriteEvent(
        val opcode: Int,
        val response: Int
    ) : WriteEvent()

    data class JS5WriteEvent(
        val indexId: Int,
        val groupId: Int,
        val compression: Int,
        val size: Int,
        val bytes: ByteBuffer
    ) : WriteEvent()
}

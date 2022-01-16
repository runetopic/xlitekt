package com.runetopic.xlitekt.network

sealed class ReadEvent {
    data class HandshakeReadEvent(
        val opcode: Int,
        val version: Int
    ) : ReadEvent()

    data class JS5ReadEvent(
        val opcode: Int,
        val indexId: Int,
        val groupId: Int
    ) : ReadEvent()
}

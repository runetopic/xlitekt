package com.runetopic.xlitekt.network.event

sealed class ReadEvent {
    data class HandshakeReadEvent(
        val opcode: Int,
        val version: Int = -1,
    ) : ReadEvent()

    data class LoginReadEvent(val idk: Int = -1) : ReadEvent()

    data class JS5ReadEvent(
        val opcode: Int,
        val indexId: Int,
        val groupId: Int
    ) : ReadEvent()
}

package com.runetopic.xlitekt.network.event

import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
sealed class ReadEvent {
    data class HandshakeReadEvent(
        val opcode: Int,
        val version: Int = -1,
    ) : ReadEvent()

    data class JS5ReadEvent(
        val opcode: Int,
        val indexId: Int,
        val groupId: Int
    ) : ReadEvent()

    data class LoginReadEvent(
        val opcode: Int,
        val clientKeys: List<Int>,
        val serverKeys: List<Int>,
        val username: String,
        val password: String,
        val clientResizeable: Boolean,
        val clientWidth: Int,
        val clientHeight: Int,
        val clientType: Int
    ) : ReadEvent()

    data class GameReadEvent(
        val opcode: Int,
        val size: Int,
        val packet: ByteReadPacket
    ) : ReadEvent()
}

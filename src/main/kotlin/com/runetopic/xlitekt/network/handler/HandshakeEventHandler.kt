package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.HANDSHAKE_JS5_OPCODE
import com.runetopic.xlitekt.network.client.ClientRequestOpcode.HANDSHAKE_LOGIN_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.CLIENT_OUTDATED_OPCODE
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.HANDSHAKE_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.application.ApplicationEnvironment

/**
 * @author Jordan Abraham
 */
class HandshakeEventHandler : EventHandler<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    private val environment by inject<ApplicationEnvironment>()
    private val clientBuild = environment.config.property("game.build.major").getString().toInt()

    override suspend fun handleEvent(client: Client, event: ReadEvent.HandshakeReadEvent): WriteEvent.HandshakeWriteEvent {
        return when (event.opcode) {
            HANDSHAKE_JS5_OPCODE -> WriteEvent.HandshakeWriteEvent(event.opcode, if (event.version == clientBuild) HANDSHAKE_SUCCESS_OPCODE else CLIENT_OUTDATED_OPCODE)
            HANDSHAKE_LOGIN_OPCODE -> WriteEvent.HandshakeWriteEvent(event.opcode, HANDSHAKE_SUCCESS_OPCODE)
            else -> throw IllegalStateException("Unhandled opcode in handshake handler. Opcode=${event.opcode}")
        }
    }
}

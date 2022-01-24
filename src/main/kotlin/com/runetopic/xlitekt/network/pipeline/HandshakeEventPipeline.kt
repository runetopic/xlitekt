package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.NetworkOpcode.HANDSHAKE_JS5_OPCODE
import com.runetopic.xlitekt.network.NetworkOpcode.HANDSHAKE_LOGIN_OPCODE
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.HANDSHAKE_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import com.runetopic.xlitekt.network.handler.LoginEventHandler
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.coroutines.withTimeout

class HandshakeEventPipeline : EventPipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    private val environment by inject<ApplicationEnvironment>()

    override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
        if (client.readChannel.availableForRead < 4) {
            withTimeout(environment.config.property("network.timeout").getString().toLong()) { client.readChannel.awaitContent() }
        }
        return when (val opcode = client.readChannel.readByte().toInt()) {
            HANDSHAKE_JS5_OPCODE -> ReadEvent.HandshakeReadEvent(opcode, client.readChannel.readInt())
            HANDSHAKE_LOGIN_OPCODE -> ReadEvent.HandshakeReadEvent(opcode)
            else -> throw IllegalStateException("Unhandled opcode found during client/server handshake. Opcode=$opcode")
        }
    }

    override suspend fun write(client: Client, event: WriteEvent.HandshakeWriteEvent) {
        client.writeChannel.writeByte(event.response.toByte())
        client.writeChannel.flush()

        if (event.response != HANDSHAKE_SUCCESS_OPCODE) {
            client.disconnect()
            return
        }

        when (event.opcode) {
            HANDSHAKE_JS5_OPCODE -> {
                client.useEventPipeline(inject<JS5EventPipeline>())
                client.useEventHandler(inject<JS5EventHandler>())
            }
            HANDSHAKE_LOGIN_OPCODE -> {
                client.writeChannel.let {
                    it.writeLong(client.seed)
                    it.flush()
                }
                client.useEventPipeline(inject<LoginEventPipeline>())
                client.useEventHandler(inject<LoginEventHandler>())
            }
        }
    }
}

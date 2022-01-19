package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.NetworkOpcode.JS5_OPCODE
import com.runetopic.xlitekt.network.NetworkOpcode.LOGIN_OPCODE
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.HANDSHAKE_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import com.runetopic.xlitekt.network.handler.LoginEventHandler
import com.runetopic.xlitekt.plugin.ktor.inject
import io.ktor.application.ApplicationEnvironment
import io.ktor.http.parametersOf
import kotlinx.coroutines.withTimeout
import org.koin.core.parameter.parametersOf

class HandshakeEventPipeline : EventPipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {
    private val environment by inject<ApplicationEnvironment>()

    override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
        if (client.readChannel.availableForRead < 4) {
            withTimeout(
                environment.config.property("network.timeout").getString().toLong()
            ) { client.readChannel.awaitContent() }
        }
        return when (val opcode = client.readChannel.readByte().toInt()) {
            JS5_OPCODE -> ReadEvent.HandshakeReadEvent(opcode, client.readChannel.readInt())
            LOGIN_OPCODE -> ReadEvent.HandshakeReadEvent(opcode)
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
            JS5_OPCODE -> {
                client.useEventPipeline(inject<JS5EventPipeline>())
                client.useEventHandler(inject<JS5EventHandler>())
            }
            LOGIN_OPCODE -> {
                val serverSeed = ((Math.random() * 99999999.0).toLong() shl 32) + (Math.random() * 99999999.0).toLong()
                client.writeChannel.writeLong(serverSeed)
                client.writeChannel.flush()
                client.useEventPipeline(inject<LoginEventPipeline> { parametersOf(serverSeed) })
                client.useEventHandler(inject<LoginEventHandler>())
            }
        }
    }
}

package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import com.runetopic.xlitekt.network.inject
import io.ktor.application.ApplicationEnvironment
import kotlinx.coroutines.withTimeout

class HandshakeEventPipeline : EventPipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    private val environment by inject<ApplicationEnvironment>()

    override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
        if (client.readChannel.availableForRead < 5) {
            withTimeout(environment.config.property("network.timeout").getString().toLong()) { client.readChannel.awaitContent() }
        }
        val opcode = client.readChannel.readByte().toInt()
        val version = client.readChannel.readInt()
        return ReadEvent.HandshakeReadEvent(opcode, version)
    }

    override suspend fun write(client: Client, event: WriteEvent.HandshakeWriteEvent) {
        if (event.opcode == 15) { // js5 request
            client.writeChannel.writeByte(event.response.toByte())
            client.writeChannel.flush()
            if (event.response == 0) {
                client.useEventPipeline(inject<JS5EventPipeline>())
                client.useEventHandler(inject<JS5EventHandler>())
            } else {
                client.disconnect()
            }
        }
    }
}

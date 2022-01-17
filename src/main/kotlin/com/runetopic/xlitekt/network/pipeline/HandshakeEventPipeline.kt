package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.IO_TIMEOUT
import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.handler.JS5EventHandler
import kotlinx.coroutines.withTimeout

class HandshakeEventPipeline : EventPipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
        if (client.readChannel.availableForRead < 5) {
            withTimeout(IO_TIMEOUT) { client.readChannel.awaitContent() }
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
                client.useEventPipeline(JS5EventPipeline())
                client.useEventHandler(JS5EventHandler())
            } else {
                client.disconnect()
            }
        }
    }
}

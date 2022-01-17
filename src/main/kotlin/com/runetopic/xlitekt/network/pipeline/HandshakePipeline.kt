package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.network.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.reactor.JS5Reactor

class HandshakePipeline : Pipeline<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    override suspend fun read(client: Client): ReadEvent.HandshakeReadEvent {
        val opcode = client.readChannel.readByte().toInt()
        val version = client.readChannel.readInt()
        return ReadEvent.HandshakeReadEvent(opcode, version)
    }

    override suspend fun write(client: Client, event: WriteEvent.HandshakeWriteEvent) {
        if (event.opcode == 15) { // js5 request
            client.writeChannel.writeByte(event.response.toByte())
            client.writeChannel.flush()
            if (event.response == 0) {
                client.usePipeline(JS5Pipeline())
                client.useReactor(JS5Reactor())
            } else {
                client.disconnect()
            }
        }
    }
}

package com.runetopic.xlitekt.network

import Client

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

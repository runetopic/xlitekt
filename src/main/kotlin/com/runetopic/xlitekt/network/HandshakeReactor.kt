package com.runetopic.xlitekt.network

import Client

class HandshakeReactor : Reactor<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    override fun process(client: Client, event: ReadEvent.HandshakeReadEvent): WriteEvent.HandshakeWriteEvent {
        val response = if (event.version == 202) 0 else 6
        return WriteEvent.HandshakeWriteEvent(event.opcode, response)
    }
}

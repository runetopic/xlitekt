package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

class HandshakeEventHandler : EventHandler<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    override fun handleEvent(client: Client, event: ReadEvent.HandshakeReadEvent): WriteEvent.HandshakeWriteEvent {
        val response = if (event.version == 202) 0 else 6
        return WriteEvent.HandshakeWriteEvent(event.opcode, response)
    }
}

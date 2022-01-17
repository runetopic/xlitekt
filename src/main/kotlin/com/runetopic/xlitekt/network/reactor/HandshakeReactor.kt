package com.runetopic.xlitekt.network.reactor

import com.runetopic.xlitekt.network.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

class HandshakeReactor : Reactor<ReadEvent.HandshakeReadEvent, WriteEvent.HandshakeWriteEvent> {

    override fun process(client: Client, event: ReadEvent.HandshakeReadEvent): WriteEvent.HandshakeWriteEvent {
        val response = if (event.version == 202) 0 else 6
        return WriteEvent.HandshakeWriteEvent(event.opcode, response)
    }
}

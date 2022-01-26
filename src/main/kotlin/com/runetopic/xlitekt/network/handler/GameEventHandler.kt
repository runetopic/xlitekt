package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

/**
 * @author Jordan Abraham
 */
class GameEventHandler : EventHandler<ReadEvent.GameReadEvent, WriteEvent.GameWriteEvent> {
    override suspend fun handleEvent(client: Client, event: ReadEvent.GameReadEvent): WriteEvent.GameWriteEvent? {
        client.readPacket(event.opcode, event.packet)
        return null
    }
}

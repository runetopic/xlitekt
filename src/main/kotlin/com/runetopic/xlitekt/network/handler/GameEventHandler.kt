package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

/**
 * @author Jordan Abraham
 */
open class GameEventHandler : EventHandler<ReadEvent.GameReadEvent, WriteEvent.GameWriteEvent> {

    override fun handleEvent(client: Client, event: ReadEvent.GameReadEvent): WriteEvent.GameWriteEvent {
        println("Handle packet.")
        return WriteEvent.GameWriteEvent(event.opcode)
    }
}

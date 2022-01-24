package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.network.packet.write.NoTimeoutPacket

/**
 * @author Jordan Abraham
 */
open class GameEventHandler : EventHandler<ReadEvent.GameReadEvent, WriteEvent.GameWriteEvent> {

    override suspend fun handleEvent(client: Client, event: ReadEvent.GameReadEvent): WriteEvent.GameWriteEvent? {
        println("Handle packet with opcode ${event.opcode} and size ${event.size}")
        // Just for now
        if (event.opcode == 12) {
            client.writePacket(NoTimeoutPacket())
        }
        return null
    }
}

package com.runetopic.xlitekt.network.handler

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

/**
 * @author Jordan Abraham
 */
interface EventHandler<R : ReadEvent, W : WriteEvent> {
    suspend fun handleEvent(client: Client, event: R): W?
}

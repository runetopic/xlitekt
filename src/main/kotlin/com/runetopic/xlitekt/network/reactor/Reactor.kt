package com.runetopic.xlitekt.network.reactor

import com.runetopic.xlitekt.network.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

interface Reactor<R : ReadEvent, W : WriteEvent> {
    fun process(client: Client, event: R): W?
}

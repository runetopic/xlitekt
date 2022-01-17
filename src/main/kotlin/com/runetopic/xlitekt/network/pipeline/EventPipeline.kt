package com.runetopic.xlitekt.network.pipeline

import com.runetopic.xlitekt.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

interface EventPipeline<R : ReadEvent, W : WriteEvent> {
    suspend fun read(client: Client): R?
    suspend fun write(client: Client, event: W)
}

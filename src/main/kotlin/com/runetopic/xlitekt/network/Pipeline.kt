package com.runetopic.xlitekt.network

import Client

interface Pipeline<R : ReadEvent, W : WriteEvent> {
    suspend fun read(client: Client): R?
    suspend fun write(client: Client, event: W)
}

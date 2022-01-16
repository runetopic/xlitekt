package com.runetopic.xlitekt.network

import Client

interface Reactor<R : ReadEvent, W : WriteEvent> {
    fun process(client: Client, event: R): W?
}

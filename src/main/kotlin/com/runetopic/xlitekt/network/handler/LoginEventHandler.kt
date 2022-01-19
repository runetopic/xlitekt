package com.runetopic.xlitekt.network.handler

import com.runetopic.cache.store.Js5Store
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.ktor.inject
import org.slf4j.Logger

class LoginEventHandler : EventHandler<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    private val store by inject<Js5Store>()
    private val logger by inject<Logger>()

    override fun handleEvent(client: Client, event: ReadEvent.LoginReadEvent): WriteEvent.LoginWriteEvent? {
        println("Login event handler")
        return null
    }
}

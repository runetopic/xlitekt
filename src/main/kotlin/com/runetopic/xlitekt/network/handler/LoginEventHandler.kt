package com.runetopic.xlitekt.network.handler

import com.runetopic.cryptography.toISAAC
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

class LoginEventHandler : EventHandler<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    override fun handleEvent(client: Client, event: ReadEvent.LoginReadEvent): WriteEvent.LoginWriteEvent {
        // TODO: Construct the player here.

        // TODO not make this fucking retarded
        client.clientCipher = event.clientKeys.toIntArray().toISAAC()
        client.serverCipher = event.serverKeys.toIntArray().toISAAC()
        return WriteEvent.LoginWriteEvent(LOGIN_SUCCESS_OPCODE, 2, 1)
    }
}

package com.runetopic.xlitekt.network.handler

import com.runetopic.cryptography.toISAAC
import com.runetopic.xlitekt.game.player.Player
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent

class LoginEventHandler : EventHandler<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    override suspend fun handleEvent(client: Client, event: ReadEvent.LoginReadEvent): WriteEvent.LoginWriteEvent {
        client.setIsaacCiphers(
            event.clientKeys.toIntArray().toISAAC(),
            event.serverKeys.toIntArray().toISAAC()
        )
        val player = Player(client)
        return WriteEvent.LoginWriteEvent(LOGIN_SUCCESS_OPCODE, 2, 1, player)
    }
}

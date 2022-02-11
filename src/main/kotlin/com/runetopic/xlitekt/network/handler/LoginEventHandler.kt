package com.runetopic.xlitekt.network.handler

import com.runetopic.cryptography.toISAAC
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.Layout
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.client.ClientResponseOpcode.LOGIN_SUCCESS_OPCODE
import com.runetopic.xlitekt.network.event.ReadEvent
import com.runetopic.xlitekt.network.event.WriteEvent
import com.runetopic.xlitekt.plugin.koin.inject

class LoginEventHandler : EventHandler<ReadEvent.LoginReadEvent, WriteEvent.LoginWriteEvent> {

    private val world by inject<World>()

    override suspend fun handleEvent(client: Client, event: ReadEvent.LoginReadEvent): WriteEvent.LoginWriteEvent {
        client.setIsaacCiphers(
            event.clientKeys.toIntArray().toISAAC(),
            event.serverKeys.toIntArray().toISAAC()
        )
        val player = Player(client, event.username)
        player.interfaceManager.currentLayout = if (event.clientResizeable) Layout.RESIZABLE else Layout.FIXED
        client.player = player
        world.players.add(player)
        return WriteEvent.LoginWriteEvent(LOGIN_SUCCESS_OPCODE)
    }
}

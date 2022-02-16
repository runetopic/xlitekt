package com.runetopic.xlitekt.game.event.impl

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.Event

sealed class Events {
    data class OnLoginEvent(
        val player: Player,
    ) : Event
}

package xlitekt.game.event.impl

import xlitekt.game.actor.player.Player
import xlitekt.game.event.Event

sealed class Events {
    data class OnLoginEvent(
        val player: Player,
    ) : Event
}

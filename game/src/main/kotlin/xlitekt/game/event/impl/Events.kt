package xlitekt.game.event.impl

import xlitekt.game.event.Event

sealed class Events {
    data class OnLoginEvent(
        val username: String,
        val password: String,
        val clientResizeable: Boolean
    ) : Event
}

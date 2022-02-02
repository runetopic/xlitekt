package com.runetopic.xlitekt.game.event

/**
 * @author Tyler Telis
 */
data class EventListener<T : Event>(
    var use: T.() -> Unit,
    var filter: (T).() -> Boolean
)

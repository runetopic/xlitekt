package com.runetopic.xlitekt.game.event.impl

import com.runetopic.xlitekt.game.event.Event

sealed class IfEvent(open val interfaceId: Int) : Event {
    data class IfOpenTopEvent(
        override val interfaceId: Int,
    ) : IfEvent(interfaceId)

    data class IfOpenSubEvent(
        override val interfaceId: Int,
        val childId: Int,
        val alwaysOpen: Boolean
    ) : IfEvent(interfaceId)

    data class IfButtonClickEvent(
        override val interfaceId: Int,
        val index: Int,
        val option: String,
        val childId: Int,
        val slotId: Int,
        val itemId: Int
    ) : IfEvent(interfaceId)
}

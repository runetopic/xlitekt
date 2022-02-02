package com.runetopic.xlitekt.game.event.impl

import com.runetopic.xlitekt.game.event.Event

data class IfButtonClickEvent(
    val index: Int,
    val interfaceId: Int,
    val option: String,
    val childId: Int,
    val slotId: Int,
    val itemId: Int
) : Event

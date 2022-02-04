package com.runetopic.xlitekt.game.event.impl

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.Event

interface IfEvent : Event

data class IfOpenTopEvent(
    val player: Player,
    val interfaceId: Int
) : IfEvent

data class IfOpenSubEvent(
    val player: Player,
    val interfaceId: Int,
    val childId: Int,
    val alwaysOpen: Boolean
) : IfEvent

data class IfButtonClickEvent(
    val player: Player,
    val interfaceId: Int,
    val index: Int,
    val option: String,
    val childId: Int,
    val slotId: Int,
    val itemId: Int
) : IfEvent

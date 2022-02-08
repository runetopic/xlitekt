package com.runetopic.xlitekt.game.event.impl

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.Event

sealed class IfEvent(open val player: Player) : Event {
    data class IfOpenEvent(
        override val player: Player,
        val interfaceId: Int,
        val childId: Int,
        val alwaysOpen: Boolean
    ) : IfEvent(player)

    data class IfButtonClickEvent(
        override val player: Player,
        val interfaceId: Int,
        val index: Int,
        val option: String,
        val childId: Int,
        val slotId: Int,
        val itemId: Int
    ) : IfEvent(player)
}



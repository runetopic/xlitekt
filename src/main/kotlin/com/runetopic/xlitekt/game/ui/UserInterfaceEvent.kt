package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player

sealed class UserInterfaceEvent {
    data class OpenEvent(
        val player: Player,
        val interfaceId: Int,
        val childId: Int
    )

    data class ButtonClickEvent(
        val player: Player,
        val interfaceId: Int,
        val childId: Int,
        val option: Int,
        val slotId: Int,
        val itemId: Int,
        val actions: String
    ) : UserInterfaceEvent()
}

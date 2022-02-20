package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.item.Item

sealed class UserInterfaceEvent {
    data class InitEvent(
        val interfaceId: Int
    ) : UserInterfaceEvent()

    data class OpenEvent(
        val interfaceId: Int
    ) : UserInterfaceEvent()

    data class CloseEvent(
        val interfaceId: Int
    ) : UserInterfaceEvent()

    data class ButtonClickEvent(
        val index: Int,
        val interfaceId: Int,
        val childId: Int,
        val slotId: Int,
        val itemId: Int,
        val action: String
    ) : UserInterfaceEvent()

    data class IfEvent(
        val slots: IntRange,
        val event: InterfaceEvent
    ) : UserInterfaceEvent()

    data class ContainerUpdateFullEvent(
        val interfaceId: Int,
        val items: List<Item?>
    ) : UserInterfaceEvent()
}

typealias OnButtonClickEvent = (Player).(UserInterfaceEvent.ButtonClickEvent) -> Unit
typealias OnOpenEvent = (Player).(UserInterfaceEvent.OpenEvent) -> Unit
typealias OnInitEvent = (Player).(UserInterfaceEvent.InitEvent) -> Unit
typealias OnCloseEvent = (Player).(UserInterfaceEvent.CloseEvent) -> Unit

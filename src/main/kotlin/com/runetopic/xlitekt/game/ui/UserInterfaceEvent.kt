package com.runetopic.xlitekt.game.ui

sealed class UserInterfaceEvent {
    data class OpenEvent(
        val interfaceId: Int,
        //val childId: Int
    ) : UserInterfaceEvent()

    data class CloseEvent(
        val interfaceId: Int,
        //val childId: Int
    ) : UserInterfaceEvent()

    data class ButtonClickEvent(
        val index: Int,
        val interfaceId: Int,
        val childId: Int,
        val option: Int,
        val slotId: Int,
        val itemId: Int,
        val action: String
    ) : UserInterfaceEvent()

    data class IfEvent(
        val slots: IntRange,
        val event: InterfaceEvent
    ) : UserInterfaceEvent()
}

typealias OnButtonClickEvent = UserInterfaceEvent.ButtonClickEvent.() -> Unit
typealias OnOpenEvent = UserInterfaceEvent.OpenEvent.() -> Unit
typealias OnCloseEvent = UserInterfaceEvent.CloseEvent.() -> Unit


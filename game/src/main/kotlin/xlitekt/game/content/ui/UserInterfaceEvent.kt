package xlitekt.game.content.ui

import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.Item

sealed class UserInterfaceEvent {
    data class CreateEvent(
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
typealias OnCreateEvent = (Player).(UserInterfaceEvent.CreateEvent) -> Unit
typealias OnCloseEvent = (Player).(UserInterfaceEvent.CloseEvent) -> Unit

package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket

sealed class UserInterfaceEvent {
    data class OpenEvent(
        val player: Player,
        val interfaceId: Int,
        val childId: Int
    ) : UserInterfaceEvent()

    data class CloseEvent(
        val player: Player,
        val interfaceId: Int,
        val childId: Int
    ) : UserInterfaceEvent()

    data class ButtonClickEvent(
        val player: Player,
        val index: Int,
        val interfaceId: Int,
        val childId: Int,
        val option: Int,
        val slotId: Int,
        val itemId: Int,
        val action: String
    ) : UserInterfaceEvent()
}

typealias OnButtonClickEvent = UserInterfaceEvent.ButtonClickEvent.() -> Unit
typealias OnOpenEvent = UserInterfaceEvent.OpenEvent.() -> Unit
typealias OnCloseEvent = UserInterfaceEvent.CloseEvent.() -> Unit

fun UserInterfaceEvent.OpenEvent.event(childId: Int, slots: IntRange, events: InterfaceEvent) = player.client?.writePacket(
    IfSetEventsPacket(
        interfaceId,
        childId,
        slots.first,
        slots.last,
        events.value
    )
)

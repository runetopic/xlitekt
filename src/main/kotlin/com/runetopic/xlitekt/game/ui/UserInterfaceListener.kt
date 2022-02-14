package com.runetopic.xlitekt.game.ui

/**
 * @author Tyler Telis
 */
abstract class UserInterfaceListener {
    private val components get() = mutableMapOf<Int, UserInterfaceEvent.ButtonClickEvent.() -> Unit>()
    var onOpenEvent: (UserInterfaceEvent.OpenEvent.() -> Unit)? = null
    var onClickEvent: (UserInterfaceEvent.ButtonClickEvent.() -> Unit)? = null

    fun onOpen(function: (UserInterfaceEvent.OpenEvent).() -> Unit) {
        this.onOpenEvent = function
    }

    fun onClick(onClickEvent: (UserInterfaceEvent.ButtonClickEvent).() -> Unit) {
        this.onClickEvent = onClickEvent
    }

    fun onClick(childId: Int, function: (UserInterfaceEvent.ButtonClickEvent).() -> Unit) {
        components[childId] = function
    }

    fun click(event: UserInterfaceEvent.ButtonClickEvent) {
        onClickEvent?.invoke(event)
        components[event.childId]?.invoke(event)
    }

    fun UserInterfaceEvent.OpenEvent.event(childId: Int, slots: IntRange, events: InterfaceEvent) = player.interfaceManager.apply {
        interfaceEvents(
            interfaceId,
            childId = childId,
            fromSlot = slots.first,
            toSlot = slots.last,
            events = events
        )
    }
}

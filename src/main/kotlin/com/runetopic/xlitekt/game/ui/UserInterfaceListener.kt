package com.runetopic.xlitekt.game.ui

typealias OpenEvent = UserInterfaceEvent.OpenEvent.() -> Unit
typealias ClickEvent = UserInterfaceEvent.ButtonClickEvent.() -> Unit

/**
 * @author Tyler Telis
 */
class UserInterfaceListener {
    private var onOpenEvent: OpenEvent? = null
    private var onClickEvent: ClickEvent? = null
    private val childIds = mutableMapOf<Int, ClickEvent>()
    private val actions = mutableMapOf<String, ClickEvent>()

    fun onOpen(onOpen: OpenEvent) {
        this.onOpenEvent = onOpen
    }

    fun open(openEvent: UserInterfaceEvent.OpenEvent) {
        this.onOpenEvent?.invoke(openEvent)
    }

    fun onClick(onClickEvent: ClickEvent) {
        this.onClickEvent = onClickEvent
    }

    fun onClick(childId: Int, function: ClickEvent) {
        this.childIds[childId] = function
    }

    fun onClick(action: String, function: ClickEvent) {
        this.actions[action] = function
    }

    fun click(event: UserInterfaceEvent.ButtonClickEvent) {
        this.onClickEvent?.invoke(event)
        this.childIds[event.childId]?.invoke(event)
        this.actions[event.action]?.invoke(event)
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

package com.runetopic.xlitekt.game.ui

/**
 * @author Tyler Telis
 */
class UserInterfaceListener {
    private var onOpenEvent: OnOpenEvent? = null
    private var onClickEvent: OnClickEvent? = null
    private val childIds = mutableMapOf<Int, OnClickEvent>()
    private val actions = mutableMapOf<String, OnClickEvent>()

    fun onOpen(onOpen: OnOpenEvent) {
        this.onOpenEvent = onOpen
    }

    fun open(openEvent: UserInterfaceEvent.OpenEvent) {
        this.onOpenEvent?.invoke(openEvent)
    }

    fun onClick(onClickEvent: OnClickEvent) {
        this.onClickEvent = onClickEvent
    }

    fun onClick(childId: Int, function: OnClickEvent) {
        this.childIds[childId] = function
    }

    fun onClick(action: String, function: OnClickEvent) {
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

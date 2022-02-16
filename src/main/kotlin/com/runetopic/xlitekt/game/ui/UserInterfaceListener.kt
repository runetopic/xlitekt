package com.runetopic.xlitekt.game.ui

/**
 * @author Tyler Telis
 */
class UserInterfaceListener {
    private var onOpenEvent: OnOpenEvent? = null
    private var onCloseEvent: OnCloseEvent? = null
    private var onButtonClickEvent: OnButtonClickEvent? = null
    private val childIds = mutableMapOf<Int, OnButtonClickEvent>()
    private val actions = mutableMapOf<String, OnButtonClickEvent>()

    fun onOpen(onOpenEvent: OnOpenEvent) {
        this.onOpenEvent = onOpenEvent
    }

    fun open(onOpenEvent: UserInterfaceEvent.OpenEvent) {
        this.onOpenEvent?.invoke(onOpenEvent)
    }

    fun onClose(onCloseEvent: OnCloseEvent) {
        this.onCloseEvent = this.onCloseEvent
    }

    fun close(closeEvent: UserInterfaceEvent.CloseEvent) {
        this.onCloseEvent?.invoke(closeEvent)
    }

    fun click(buttonClickEvent: UserInterfaceEvent.ButtonClickEvent) {
        this.onButtonClickEvent?.invoke(buttonClickEvent)
        this.childIds[buttonClickEvent.childId]?.invoke(buttonClickEvent)
        this.actions[buttonClickEvent.action]?.invoke(buttonClickEvent)
    }

    fun onClick(onButtonClickEvent: OnButtonClickEvent) {
        this.onButtonClickEvent = onButtonClickEvent
    }

    fun onClick(childId: Int, onButtonClickEvent: OnButtonClickEvent) {
        this.childIds[childId] = onButtonClickEvent
    }

    fun onClick(action: String, onButtonClickEvent: OnButtonClickEvent) {
        this.actions[action] = onButtonClickEvent
    }
}

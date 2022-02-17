package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.item.Item
import com.runetopic.xlitekt.shared.packInterface

/**
 * @author Tyler Telis
 */
class UserInterfaceListener(
    val player: Player,
    val userInterface: UserInterface
) {
    private var onOpenEvent: OnOpenEvent? = null
    private var onCloseEvent: OnCloseEvent? = null
    private var onButtonClickEvent: OnButtonClickEvent? = null
    private val children = mutableMapOf<Int, OnButtonClickEvent>()
    private val actions = mutableMapOf<String, OnButtonClickEvent>()
    private val texts = mutableMapOf<Int, String>()
    private val items = mutableMapOf<Int, UserInterfaceEvent.ContainerUpdateFullEvent>()
    private val events = mutableMapOf<Int, UserInterfaceEvent.IfEvent>()

    fun onOpen(onOpenEvent: OnOpenEvent) {
        this.onOpenEvent = onOpenEvent
    }

    fun open(onOpenEvent: UserInterfaceEvent.OpenEvent) {
        this.onOpenEvent?.invoke(onOpenEvent)
        this.texts.forEach { player.interfaceManager.setText(it.key, it.value) }
        this.events.forEach { player.interfaceManager.setEvent(it.key, it.value) }
        this.items.forEach {
            player.interfaceManager.setContainerUpdateFull(
                interfaceId = it.value.interfaceId,
                containerKey = it.key,
                items = it.value.items
            )
        }
    }

    fun onClose(onCloseEvent: OnCloseEvent) {
        this.onCloseEvent = onCloseEvent
    }

    fun close(closeEvent: UserInterfaceEvent.CloseEvent) {
        this.onCloseEvent?.invoke(closeEvent)
    }

    fun click(buttonClickEvent: UserInterfaceEvent.ButtonClickEvent) {
        this.onButtonClickEvent?.invoke(buttonClickEvent)
        this.children[buttonClickEvent.childId]?.invoke(buttonClickEvent)
        this.actions[buttonClickEvent.action]?.invoke(buttonClickEvent)
    }

    fun onClick(onButtonClickEvent: OnButtonClickEvent) {
        this.onButtonClickEvent = onButtonClickEvent
    }

    fun onClick(childId: Int, onButtonClickEvent: OnButtonClickEvent) {
        this.children[childId] = onButtonClickEvent
    }

    fun onClick(action: String, onButtonClickEvent: OnButtonClickEvent) {
        this.actions[action] = onButtonClickEvent
    }

    fun UserInterfaceEvent.OpenEvent.setText(childId: Int, text: String) {
        texts[interfaceId.packInterface(childId)] = text
    }

    fun UserInterfaceEvent.OpenEvent.setEvent(childId: Int, slots: IntRange, event: InterfaceEvent) {
        events[interfaceId.packInterface(childId)] = UserInterfaceEvent.IfEvent(slots, event)
    }

    fun UserInterfaceEvent.OpenEvent.setItems(containerKey: Int, item: List<Item?>) {
        items[containerKey] = UserInterfaceEvent.ContainerUpdateFullEvent(
            interfaceId = -1,
            item
        )
    }
}

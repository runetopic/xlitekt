package xlitekt.game.content.ui

import xlitekt.game.actor.player.Player
import xlitekt.game.content.item.Item
import xlitekt.shared.packInterface

/**
 * @author Tyler Telis
 */
class UserInterfaceListener(
    private val player: Player,
    val userInterface: UserInterface
) {
    private var onCreateEvent: OnCreateEvent? = null
    private var onOpenEvent: OnOpenEvent? = null
    private var onCloseEvent: OnCloseEvent? = null
    private var onButtonClickEvent: OnButtonClickEvent? = null
    private var onOperation: OnOpHeldEvent? = null
    private val children = mutableMapOf<Int, OnButtonClickEvent>()
    private val actions = mutableMapOf<String, OnButtonClickEvent>()
    private val operations = mutableMapOf<Int, OnOpHeldEvent>()
    private val texts = mutableMapOf<Int, String>()
    private val items = mutableMapOf<Int, UserInterfaceEvent.ContainerUpdateFullEvent>()
    private val events = mutableMapOf<Int, UserInterfaceEvent.IfEvent>()

    fun onOpen(onOpenEvent: OnOpenEvent) {
        this.onOpenEvent = onOpenEvent
    }

    fun onCreate(onCreateEvent: OnCreateEvent) {
        this.onCreateEvent = onCreateEvent
    }

    fun init(createEvent: UserInterfaceEvent.CreateEvent) {
        this.onCreateEvent?.invoke(player, createEvent)
    }

    fun open(openEvent: UserInterfaceEvent.OpenEvent) {
        this.onOpenEvent?.invoke(player, openEvent)
        this.texts.forEach { player.interfaces.setText(it.key, it.value) }
        this.events.forEach { player.interfaces.setEvent(it.key, it.value) }
        this.items.forEach {
            player.interfaces.setContainerUpdateFull(
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
        this.onCloseEvent?.invoke(player, closeEvent)
    }

    fun click(buttonClickEvent: UserInterfaceEvent.ButtonClickEvent) {
        this.onButtonClickEvent?.invoke(player, buttonClickEvent)
        this.children[buttonClickEvent.childId]?.invoke(player, buttonClickEvent)
        this.actions[buttonClickEvent.action]?.invoke(player, buttonClickEvent)
    }

    fun opHeld(opHeldEvent: UserInterfaceEvent.OpHeldEvent) {
        this.onOperation?.invoke(player, opHeldEvent)
        this.operations[opHeldEvent.fromInterfaceId]?.invoke(player, opHeldEvent)
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

    fun onOpHeld(event: OnOpHeldEvent) {
        this.onOperation = event
    }

    fun setText(childId: Int, text: String) {
        texts[userInterface.interfaceInfo.id.packInterface(childId)] = text
    }

    fun setEvent(childId: Int, slots: IntRange, event: InterfaceEvent) {
        events[userInterface.interfaceInfo.id.packInterface(childId)] = UserInterfaceEvent.IfEvent(slots, event)
    }

    fun setItems(containerKey: Int, item: List<Item?>) {
        items[containerKey] = UserInterfaceEvent.ContainerUpdateFullEvent(
            interfaceId = -1,
            item
        )
    }
}

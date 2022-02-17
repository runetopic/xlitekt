package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.enum.EnumEntryType
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.item.Item
import com.runetopic.xlitekt.game.ui.InterfaceMapping.addInterfaceListener
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.UpdateContainerFullPacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.shared.packInterface

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    private val interfaces = mutableListOf<UserInterface>()
    val listeners = mutableListOf<UserInterfaceListener>()

    var currentInterfaceLayout = InterfaceLayout.FIXED

    fun login() {
        openTop(currentInterfaceLayout.interfaceId)
        gameInterfaces.forEach { it.interfaceInfo.resizableChildId?.let { childId -> openInterface(it, childId) } }
        player.write(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        message("Welcome to Xlitekt.")
    }

    fun switchLayout(toLayout: InterfaceLayout) {
        if (toLayout == currentInterfaceLayout) return
        openTop(toLayout.interfaceId)
        if (modalOpen()) closeModal()
        gameInterfaces.forEach { moveSub(it, toLayout) }
        currentInterfaceLayout = toLayout
    }

    fun message(message: String) { // TODO Move this to a social thing.
        player.write(MessageGamePacket(0, message, false))
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) { // TODO Move this somewhere else not rlly interface related.
        player.write(RunClientScriptPacket(scriptId, parameters))
    }

    fun closeModal() = interfaces.find { (it.interfaceInfo.resizableChildId ?: MODAL_CHILD_ID).enumChildForLayout(currentInterfaceLayout) == MODAL_CHILD_ID.enumChildForLayout(currentInterfaceLayout) }?.run {
        closeInterface(this, MODAL_CHILD_ID)
    }

    fun closeInventory() = interfaces.find { (it.interfaceInfo.resizableChildId ?: INVENTORY_CHILD_ID).enumChildForLayout(currentInterfaceLayout) == INVENTORY_CHILD_ID.enumChildForLayout(currentInterfaceLayout) }?.run {
        closeInterface(this, INVENTORY_CHILD_ID)
    }

    fun setText(packedInterface: Int, text: String) = player.write(
        IfSetTextPacket(
            packedInterface = packedInterface,
            text = text
        )
    )

    fun setEvent(packedInterface: Int, ifEvent: UserInterfaceEvent.IfEvent) = player.write(
        IfSetEventsPacket(
            packedInterface = packedInterface,
            fromSlot = ifEvent.slots.first,
            toSlot = ifEvent.slots.last,
            event = ifEvent.event.value
        )
    )

    fun setContainerUpdateFull(containerKey: Int, interfaceId: Int, childId: Int = 65536, items: List<Item?>) {
        player.write(
            UpdateContainerFullPacket(
                packedInterface = interfaceId.packInterface(childId),
                containerKey = containerKey,
                items = items
            )
        )
    }

    private fun openTop(id: Int) = player.write(IfOpenTopPacket(interfaceId = id))

    fun openModal(userInterface: UserInterface) {
        if (modalOpen()) closeModal()
        openInterface(userInterface, MODAL_CHILD_ID)
    }

    fun openInventory(userInterface: UserInterface) {
        openInterface(userInterface, INVENTORY_CHILD_ID)
    }

    private fun openInterface(userInterface: UserInterface, derivedChildId: Int) = userInterface.let {
        interfaces += it
        val childId = derivedChildId.enumChildForLayout(
            currentInterfaceLayout
        )
        player.write(
            IfOpenSubPacket(
                interfaceId = it.interfaceInfo.id,
                toPackedInterface = currentInterfaceLayout.interfaceId.packInterface(childId),
                alwaysOpen = true
            )
        )
        addInterfaceListener(it, player)
    }.run {
        listeners += this
        open(
            UserInterfaceEvent.OpenEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    private fun modalOpen(): Boolean = interfaces.find {
        val derivedChildId = (it.interfaceInfo.resizableChildId ?: MODAL_CHILD_ID)
        derivedChildId.enumChildForLayout(currentInterfaceLayout) == MODAL_CHILD_ID.enumChildForLayout(currentInterfaceLayout)
    } != null

    private fun inventoryOpen(): Boolean = interfaces.find {
        val derivedChildId = (it.interfaceInfo.resizableChildId ?: 73)
        derivedChildId.enumChildForLayout(currentInterfaceLayout) == 73.enumChildForLayout(currentInterfaceLayout)
    } != null

    private fun closeInterface(userInterface: UserInterface, childId: Int) = userInterface.let {
        interfaces -= it

        player.write(
            IfCloseSubPacket(
                packedInterface = currentInterfaceLayout.interfaceId.packInterface((it.interfaceInfo.resizableChildId ?: childId).enumChildForLayout(currentInterfaceLayout))
            )
        )
        listeners.find { listener -> listener.userInterface == it }
    }?.run {
        listeners.removeAt(listeners.indexOf(this))
        close(
            UserInterfaceEvent.CloseEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    private fun moveSub(userInterface: UserInterface, toLayout: InterfaceLayout) = userInterface.let {
        val derivedChildId = (it.interfaceInfo.resizableChildId ?: MODAL_CHILD_ID)
        val fromChildId = derivedChildId.enumChildForLayout(currentInterfaceLayout)
        val toChildId = derivedChildId.enumChildForLayout(toLayout)
        player.write(
            IfMoveSubPacket(
                fromPackedInterface = currentInterfaceLayout.interfaceId.packInterface(fromChildId),
                toPackedInterface = toLayout.interfaceId.packInterface(toChildId)
            )
        )
        listeners.find { listener -> listener.userInterface == it }
    }?.run {
        open(
            UserInterfaceEvent.OpenEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    private fun Int.enumChildForLayout(layout: InterfaceLayout): Int =
        entryType<EnumEntryType>(layout.enumId)
            ?.params
            ?.entries
            ?.find { it.key == InterfaceLayout.RESIZABLE.interfaceId.packInterface(this) }
            ?.value as Int and 0xffff

    private companion object {
        val gameInterfaces = setOf(
            UserInterface.AccountManagement,
            UserInterface.Settings,
            UserInterface.Inventory,
            UserInterface.MiniMap,
            UserInterface.ChatBox,
            UserInterface.Logout,
            UserInterface.Emotes,
            UserInterface.Magic,
            UserInterface.MusicPlayer,
            UserInterface.Skills,
            UserInterface.WornEquipment,
            UserInterface.Friends,
            UserInterface.Prayer,
            UserInterface.CombatOptions,
            UserInterface.CharacterSummary,
            UserInterface.UnknownOverlay,
            UserInterface.ChatChannel
        )

        const val MODAL_CHILD_ID = 16
        const val INVENTORY_CHILD_ID = 73
    }
}

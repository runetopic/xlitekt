package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.cache.Cache.entryType
import com.runetopic.xlitekt.cache.provider.config.enum.EnumEntryType
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceMapping.addInterfaceListener
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
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
        gameInterfaces.forEach(::openInterface)
        player.client?.writePacket(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        message("Welcome to Xlitekt.")
    }

    fun switchLayout(toLayout: InterfaceLayout) {
        if (toLayout == currentInterfaceLayout) return
        openTop(toLayout.interfaceId)
        closeModal()
        gameInterfaces.forEach { moveSub(it, toLayout) }
        currentInterfaceLayout = toLayout
    }

    fun message(message: String) { // TODO Move this to a social thing.
        player.client?.writePacket(MessageGamePacket(0, message, false))
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) { // TODO Move this somewhere else not rlly interface related.
        player.client?.writePacket(RunClientScriptPacket(scriptId, parameters))
    }

    fun closeModal() = interfaces.find { (it.interfaceInfo.resizableChildId ?: MODAL_CHILD).enumChildForLayout(currentInterfaceLayout) == MODAL_CHILD.enumChildForLayout(currentInterfaceLayout) }?.run {
        closeInterface(this)
    }

    fun setText(packedInterface: Int, text: String) = player.client?.writePacket(
        IfSetTextPacket(
            packedInterface = packedInterface,
            text = text
        )
    )

    fun setEvent(packedInterface: Int, ifEvent: UserInterfaceEvent.IfEvent) = player.client?.writePacket(
        IfSetEventsPacket(
            packedInterface = packedInterface,
            fromSlot = ifEvent.slots.first,
            toSlot = ifEvent.slots.last,
            event = ifEvent.event.value
        )
    )

    private fun openTop(id: Int) = player.client?.writePacket(IfOpenTopPacket(interfaceId = id))

    fun openInterface(userInterface: UserInterface) = userInterface.let {
        interfaces += it
        val derivedChildId = (it.interfaceInfo.resizableChildId ?: MODAL_CHILD)
        val childId = derivedChildId.enumChildForLayout(
            currentInterfaceLayout
        )
        player.client?.writePacket(
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

    private fun closeInterface(userInterface: UserInterface) = userInterface.let {
        interfaces -= it

        player.client?.writePacket(
            IfCloseSubPacket(
                packedInterface = currentInterfaceLayout.interfaceId.packInterface((it.interfaceInfo.resizableChildId ?: MODAL_CHILD).enumChildForLayout(currentInterfaceLayout))
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
        val derivedChildId = (it.interfaceInfo.resizableChildId ?: MODAL_CHILD)
        val fromChildId = derivedChildId.enumChildForLayout(currentInterfaceLayout)
        val toChildId = derivedChildId.enumChildForLayout(toLayout)
        player.client?.writePacket(
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

        const val MODAL_CHILD = 16
    }
}

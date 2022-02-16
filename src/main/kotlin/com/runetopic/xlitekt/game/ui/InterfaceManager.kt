package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceMapping.addInterfaceListener
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceInfo
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
import kotlinx.coroutines.runBlocking

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    var currentInterfaceLayout = InterfaceLayout.FIXED
    private val interfaces = mutableListOf<UserInterface>()

    var listeners = mutableListOf<UserInterfaceListener>()

    fun login() {
        openTop(currentInterfaceLayout.interfaceId)
        gameInterfaces.forEach(::openInterface)
        player.client?.writePacket(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        message("Welcome to Xlitekt.")
    }

    private fun openTop(id: Int) = player.client?.writePacket(IfOpenTopPacket(interfaceId = id))

    fun openInterface(userInterface: UserInterface) = userInterface.let {
        interfaces += it

        val childId = it.interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)

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

    fun closeInterface(userInterface: UserInterface) = userInterface.let {
        interfaces -= it

        val childId = it.interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)

        player.client?.writePacket(
            IfCloseSubPacket(
                packedInterface = currentInterfaceLayout.interfaceId.packInterface(childId)
            )
        )
        listeners.find { l -> l.userInterface == it }
    }?.run {
        listeners.removeAt(listeners.indexOf(this))

        close(
            UserInterfaceEvent.CloseEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    fun switchLayout(toLayout: InterfaceLayout) {
        if (toLayout == currentInterfaceLayout) return
        openTop(toLayout.interfaceId)
        interfaces.clear()
        gameInterfaces.forEach { moveSub(it, toLayout) }
        currentInterfaceLayout = toLayout
    }

    private fun moveSub(userInterface: UserInterface, toLayout: InterfaceLayout) = userInterface.let {
        val fromChildId = it.interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)
        val toChildId = it.interfaceInfo.destination.childIdForLayout(toLayout)
        player.client?.writePacket(
            IfMoveSubPacket(
                fromPackedInterface = currentInterfaceLayout.interfaceId.packInterface(fromChildId),
                toPackedInterface = toLayout.interfaceId.packInterface(toChildId)
            )
        )
        listeners.find { l -> l.userInterface == it }
    }?.run {
        open(
            UserInterfaceEvent.OpenEvent(
                interfaceId = userInterface.interfaceInfo.id,
            )
        )
    }

    fun message(message: String) {
        player.client?.writePacket(MessageGamePacket(0, message, false))
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) = runBlocking {
        player.client?.writePacket(RunClientScriptPacket(scriptId, parameters))
    }

    fun closeLastInterface(): Unit = closeInterface(interfaces.last()) ?: throw IllegalStateException("Couldn't close last interface. ${interfaces.last()}") // oh cause now were returning a unit u can return a unit but its nullable yeah but the handler doesnt want a unit

    fun setText(packedInterface: Int, text: String) {
        player.client?.writePacket(
            IfSetTextPacket(
                packedInterface = packedInterface,
                text = text
            )
        )
    }

    fun setEvent(packedInterface: Int, ifEvent: UserInterfaceEvent.IfEvent) = player.client?.writePacket(
        IfSetEventsPacket(
            packedInterface,
            ifEvent.slots.first,
            ifEvent.slots.last,
            ifEvent.event.value
        )
    )

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
    }
}

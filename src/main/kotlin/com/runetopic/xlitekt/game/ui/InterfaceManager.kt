package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceInfo
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceListener
import com.runetopic.xlitekt.network.packet.IfCloseSubPacket
import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.shared.buffer.packInterface
import kotlinx.coroutines.runBlocking

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    var currentInterfaceLayout = InterfaceLayout.FIXED
    private val interfaces = mutableListOf<UserInterface>()

    fun login() {
        openTop(currentInterfaceLayout.interfaceId)
        gameInterfaces.forEach(::openInterface)
        player.client?.writePacket(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        message("Welcome to Xlitekt.")
    }

    private fun openTop(id: Int) = player.client?.writePacket(IfOpenTopPacket(interfaceId = id))

    fun openInterface(userInterface: UserInterface) {
        interfaces += userInterface

        val interfaceInfo = interfaceInfo(userInterface.name)
        val childId = interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)

        player.client?.writePacket(
            IfOpenSubPacket(
                interfaceId = interfaceInfo.id,
                toPackedInterface = currentInterfaceLayout.interfaceId.packInterface(childId),
                alwaysOpen = true
            )
        )

        interfaceListener(userInterface)?.open(
            UserInterfaceEvent.OpenEvent(
                player = player,
                interfaceId = interfaceInfo.id,
                childId = childId
            )
        )
    }

    fun closeInterface(userInterface: UserInterface) {
        interfaces -= userInterface

        val interfaceInfo = userInterface.interfaceInfo
        val childId = interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)

        player.client?.writePacket(
            IfCloseSubPacket(
                packedInterface = currentInterfaceLayout.interfaceId.packInterface(childId)
            )
        )

        interfaceListener(userInterface)?.close(
            UserInterfaceEvent.CloseEvent(
                player = player,
                interfaceId = interfaceInfo.id,
                childId = childId
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

    private fun moveSub(userInterface: UserInterface, toLayout: InterfaceLayout) {
        val interfaceId = userInterface.interfaceInfo.id
        val fromChildId = userInterface.interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)
        val toChildId = userInterface.interfaceInfo.destination.childIdForLayout(toLayout)
        player.client?.writePacket(
            IfMoveSubPacket(
                fromPackedInterface = currentInterfaceLayout.interfaceId.packInterface(fromChildId),
                toPackedInterface = toLayout.interfaceId.packInterface(toChildId)
            )
        )
        interfaceListener(userInterface)?.open(
            UserInterfaceEvent.OpenEvent(
                player = player,
                interfaceId = interfaceId,
                childId = toChildId
            )
        )
    }

    fun message(message: String) {
        player.client?.writePacket(MessageGamePacket(0, message, false))
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) = runBlocking {
        player.client?.writePacket(RunClientScriptPacket(scriptId, parameters))
    }

    fun closeLastInterface() = closeInterface(interfaces.last())

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

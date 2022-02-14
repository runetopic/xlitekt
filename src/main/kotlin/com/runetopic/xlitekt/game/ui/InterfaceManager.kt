package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceInfo
import com.runetopic.xlitekt.game.ui.InterfaceMapping.interfaceListeners
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.IfSetEventsPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.util.ext.packInterface

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    var currentInterfaceLayout = InterfaceLayout.FIXED

    private val open = mutableListOf<UserInterface>()

    fun login() {
        openTop(currentInterfaceLayout.interfaceId)
        gameInterfaces.forEach { open += it }
        player.client.writePacket(VarpSmallPacket(1737, -1)) // TODO TEMP until i write a var system
        message("Welcome to Xlitekt.")
    }

    private fun openTop(id: Int) = player.client.writePacket(IfOpenTopPacket(interfaceId = id))

    operator fun MutableCollection<in UserInterface>.plusAssign(element: UserInterface) {
        this.add(element)
        openSub(element)
    }

    private fun openSub(element: UserInterface) {
        val interfaceInfo = interfaceInfo(element.name)
        val childId = interfaceInfo.destination.childIdForLayout(currentInterfaceLayout)

        interfaceListeners[element::class]?.invoke(element)

        element.onOpenEvent?.invoke(
            UserInterfaceEvent.OpenEvent(
                player = player,
                interfaceId = interfaceInfo.id,
                childId = childId
            )
        )

        player.client.writePacket(
            IfOpenSubPacket(
                interfaceId = interfaceInfo.id,
                toPackedInterface = currentInterfaceLayout.interfaceId.packInterface(childId),
                alwaysOpen = true
            )
        )
    }

    fun message(message: String) {
        player.client.writePacket(MessageGamePacket(0, message, false))
    }

    fun runClientScript(scriptId: Int, parameters: List<Any>) = player.client.writePacket(RunClientScriptPacket(scriptId, parameters))

    fun interfaceEvents(interfaceId: Int, childId: Int, fromSlot: Int, toSlot: Int, events: InterfaceEvent) {
        player.client.writePacket(
            IfSetEventsPacket(
                interfaceId,
                childId,
                fromSlot,
                toSlot,
                events.value
            )
        )
    }

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
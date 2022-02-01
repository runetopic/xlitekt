package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    private val open = mutableMapOf<Int, Int>()

    suspend fun login() {
        player.displayMode.let { displayMode ->
            openTop(displayMode.interfaceId)
            sendInterfacesForDisplay(displayMode)
        }
    }

    private suspend fun sendInterfacesForDisplay(displayMode: DisplayMode) {
        InterfaceInfo.values().forEach {
            val interfaceId = it.interfaceId
            if (interfaceId == -1) return@forEach
            val componentId = it.componentIdForDisplay(displayMode)
            if (componentId == -1) return@forEach
            openSub(it.interfaceId, componentId)
        }
    }

    private fun open(packed: Int, interfaceId: Int): Boolean {
        if (open.containsKey(packed)) return false // TODO handle this better
        open[packed] = interfaceId
        return true
    }

    private suspend fun openTop(interfaceId: Int) {
        val packed = interfaceId.packInterfaceComponent()
        open(packed, interfaceId)
        player.client.writePacket(IfOpenTopPacket(interfaceId))
        // TODO emit events when I add the event bus system so content can react to IF events
    }

    private suspend fun openSub(interfaceId: Int, component: Int) {
        val packed = player.displayMode.interfaceId.packInterfaceComponent(component)
        open(packed, interfaceId)
        player.client.writePacket(IfOpenSubPacket(interfaceId, packed, true))
        // TODO emit events when I add the event bus system so content can react to IF events
    }

    private fun Int.packInterfaceComponent(component: Int = 0) = this.shl(16).or(component)
}

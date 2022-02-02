package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.plugin.ktor.inject

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player
) {
    private val open = mutableMapOf<Int, Int>()
    private val eventBus by inject<EventBus>()

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
            openSub(it.interfaceId, componentId, true)
        }
    }

    private fun open(packed: Int, interfaceId: Int): Boolean {
        if (open.containsKey(packed)) return false
        open[packed] = interfaceId
        return true
    }

    private suspend fun openTop(interfaceId: Int) {
        val packed = interfaceId.packInterfaceComponent()
        if (open(packed, interfaceId)) {
            eventBus.notify(IfEvent.IfOpenTop(interfaceId))
            player.client.writePacket(IfOpenTopPacket(interfaceId))
        }
    }

    private suspend fun openSub(interfaceId: Int, childId: Int, alwaysOpen: Boolean) {
        val packed = player.displayMode.interfaceId.packInterfaceComponent(childId)
        if (open(packed, interfaceId)) {
            eventBus.notify(IfEvent.IfOpenSub(interfaceId, childId, alwaysOpen))
            player.client.writePacket(IfOpenSubPacket(interfaceId, packed, alwaysOpen))
        }
    }

    private fun Int.packInterfaceComponent(component: Int = 0) = this.shl(16).or(component)
}

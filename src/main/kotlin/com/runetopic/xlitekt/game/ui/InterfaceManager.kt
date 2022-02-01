package com.runetopic.xlitekt.game.ui

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket

/**
 * @author Tyler Telis
 */
class InterfaceManager(
    private val player: Player,
    private var displayMode: DisplayMode
) {
    private val open = mutableMapOf<Int, Int>()
    private val logger = InlineLogger()

    suspend fun login() {
        openTop(displayMode.interfaceId)
        InterfaceInfo.values().forEach { openSub(it.interfaceId, it.componentIdForDisplay(displayMode)) }
    }

    private suspend fun openTop(interfaceId: Int) {
        val packed = interfaceId.packInterfaceComponent()
        open[packed] = interfaceId
        player.client.writePacket(IfOpenTopPacket(interfaceId))
    }

    private suspend fun openSub(interfaceId: Int, component: Int) {
        val packed = displayMode.interfaceId.packInterfaceComponent(component)
        open[packed] = interfaceId
        player.client.writePacket(IfOpenSubPacket(interfaceId, packed, true))
    }

    private fun Int.packInterfaceComponent(component: Int = 0) = this.shl(16).or(component)
}

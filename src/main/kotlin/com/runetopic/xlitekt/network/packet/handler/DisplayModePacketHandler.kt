package com.runetopic.xlitekt.network.packet.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.DisplayMode
import com.runetopic.xlitekt.network.packet.DisplayModePacket

/**
 * @author Tyler Telis
 */
class DisplayModePacketHandler : PacketHandler<DisplayModePacket> {

    override suspend fun handlePacket(player: Player, packet: DisplayModePacket) {
        val displayMode = DisplayMode.values().find { it.mode == packet.mode }
            ?: throw IllegalStateException("Unhandled display mode sent from client. Mode=${packet.mode} Width=${packet.width} Height=${packet.height}")
        player.interfaceManager.switchDisplayMode(displayMode)
    }
}

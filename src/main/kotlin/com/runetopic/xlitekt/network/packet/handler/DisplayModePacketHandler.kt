package com.runetopic.xlitekt.network.packet.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.network.packet.DisplayModePacket

/**
 * @author Tyler Telis
 */
class DisplayModePacketHandler : PacketHandler<DisplayModePacket> {

    override suspend fun handlePacket(player: Player, packet: DisplayModePacket) {
        val interfaceLayout = InterfaceLayout.values().find { it.id == packet.displayMode }
            ?: throw IllegalStateException("Unhandled display mode sent from client. Mode=${packet.displayMode} Width=${packet.width} Height=${packet.height}")
        player.interfaceManager.switchLayout(interfaceLayout)
    }
}

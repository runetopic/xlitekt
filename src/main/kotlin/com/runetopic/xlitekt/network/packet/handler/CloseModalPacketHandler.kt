package com.runetopic.xlitekt.network.packet.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.CloseModalPacket

class CloseModalPacketHandler : PacketHandler<CloseModalPacket> {
    override fun handlePacket(player: Player, packet: CloseModalPacket) {
        player.interfaceManager.closeModal()
    }
}

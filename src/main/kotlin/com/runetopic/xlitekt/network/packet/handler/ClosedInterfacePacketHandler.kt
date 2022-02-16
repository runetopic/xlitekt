package com.runetopic.xlitekt.network.packet.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.ClosedInterfacePacket

class ClosedInterfacePacketHandler : PacketHandler<ClosedInterfacePacket> {
    override fun handlePacket(player: Player, packet: ClosedInterfacePacket) = player.interfaceManager.closeLastInterface()
}

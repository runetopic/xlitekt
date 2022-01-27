package com.runetopic.xlitekt.network.packet.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.Packet

/**
 * @author Jordan Abraham
 */
interface PacketHandler<out P : Packet> {
    suspend fun handlePacket(player: Player, packet: @UnsafeVariance P)
}

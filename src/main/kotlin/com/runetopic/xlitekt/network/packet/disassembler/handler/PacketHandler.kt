package com.runetopic.xlitekt.network.packet.disassembler.handler

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PacketHandler<out P : Packet>(
    val player: Player,
    val packet: P
)

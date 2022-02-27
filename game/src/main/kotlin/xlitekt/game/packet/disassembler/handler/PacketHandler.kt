package xlitekt.game.packet.disassembler.handler

import xlitekt.game.actor.player.Player
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PacketHandler<out P : Packet>(
    val player: Player,
    val packet: P
)

package xlitekt.game.packet

import xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport
) : Packet

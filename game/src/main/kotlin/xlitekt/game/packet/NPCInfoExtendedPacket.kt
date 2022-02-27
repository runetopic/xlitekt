package xlitekt.game.packet

import xlitekt.game.actor.player.Viewport

/**
 * @author Tyler Telis
 */
data class NPCInfoExtendedPacket(
    val viewport: Viewport
) : Packet

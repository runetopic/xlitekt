package xlitekt.game.packet

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val playerLocations: Map<Player, Location>
) : Packet

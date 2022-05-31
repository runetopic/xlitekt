package xlitekt.game.packet

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 */
data class RebuildNormalPacket(
    val viewport: Viewport,
    val location: Location,
    val update: Boolean,
    val players: NonBlockingHashMapLong<Player>
) : Packet

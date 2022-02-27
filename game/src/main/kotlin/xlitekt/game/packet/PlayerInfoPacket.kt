package xlitekt.game.packet

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.world.map.location.Location

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val viewport: Viewport,
    val updates: Map<Player, ByteReadPacket>,
    val locations: Map<Player, Location>
) : Packet

package xlitekt.game.packet

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.movement.MovementStep
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
    val previousLocations: Map<Player, Location?>,
    val locations: Map<Player, Location>,
    val steps: Map<Player, MovementStep?>
) : Packet

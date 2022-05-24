package xlitekt.game.packet

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: Map<Int, Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: Map<Player, ByteArray>,
    val lowDefinitionUpdates: Map<Player, ByteArray>,
    val steps: Map<Player, MovementStep?>
) : Packet

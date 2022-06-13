package xlitekt.game.packet

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: NonBlockingHashMapLong<Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: Array<ByteArray?>,
    val lowDefinitionUpdates: Array<ByteArray?>,
    val alternativeHighDefinitionUpdates: Array<ByteArray?>,
    val alternativeLowDefinitionUpdates: Array<ByteArray?>,
    val movementStepsUpdates: Array<MovementStep?>
) : Packet

package xlitekt.game.packet

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import java.util.*

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: NonBlockingHashMapLong<Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    val lowDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    val alternativeHighDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    val alternativeLowDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    val movementStepsUpdates: NonBlockingHashMapLong<Optional<MovementStep>>
) : Packet

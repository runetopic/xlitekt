package xlitekt.game.packet

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Viewport
import java.util.*

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val highDefinitionUpdates: NonBlockingHashMapLong<Optional<ByteArray>>,
    val movementStepsUpdates: NonBlockingHashMapLong<Optional<MovementStep>>
) : Packet

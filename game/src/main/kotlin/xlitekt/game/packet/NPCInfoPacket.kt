package xlitekt.game.packet

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Viewport
import java.util.Optional

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val movementStepsUpdates: Map<Int, Optional<MovementStep>>
) : Packet

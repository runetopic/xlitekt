package xlitekt.game.packet

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val highDefinitionUpdates: Array<ByteArray?>,
    val movementStepsUpdates: Array<MovementStep?>
) : Packet

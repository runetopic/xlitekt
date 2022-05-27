package xlitekt.game.packet

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import java.util.Optional

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: Map<Int, Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: Map<Int, Optional<ByteArray>>,
    val lowDefinitionUpdates: Map<Int, Optional<ByteArray>>,
    val alternativeHighDefinitionUpdates: Map<Int, Optional<ByteArray>>,
    val alternativeLowDefinitionUpdates: Map<Int, Optional<ByteArray>>,
    val movementStepsUpdates: Map<Int, Optional<MovementStep>>
) : Packet

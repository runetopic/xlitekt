package xlitekt.game.packet

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.tick.AlternativeHighDefinitionUpdates
import xlitekt.game.tick.AlternativeLowDefinitionUpdates
import xlitekt.game.tick.HighDefinitionUpdates
import xlitekt.game.tick.LowDefinitionUpdates
import xlitekt.game.tick.PlayerMovementStepsUpdates

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: Map<Int, Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: HighDefinitionUpdates,
    val lowDefinitionUpdates: LowDefinitionUpdates,
    val alternativeHighDefinitionUpdates: AlternativeHighDefinitionUpdates,
    val alternativeLowDefinitionUpdates: AlternativeLowDefinitionUpdates,
    val movementStepsUpdates: PlayerMovementStepsUpdates
) : Packet

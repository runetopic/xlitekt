package xlitekt.game.packet

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.Viewport
import xlitekt.game.tick.PlayerUpdates.AlternativeHighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.AlternativeLowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.HighDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.LowDefinitionPlayerUpdates
import xlitekt.game.tick.PlayerUpdates.MovementStepsPlayerUpdates

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
data class PlayerInfoPacket(
    val players: Map<Int, Player>,
    val viewport: Viewport,
    val highDefinitionUpdates: HighDefinitionPlayerUpdates,
    val lowDefinitionUpdates: LowDefinitionPlayerUpdates,
    val alternativeHighDefinitionUpdates: AlternativeHighDefinitionPlayerUpdates,
    val alternativeLowDefinitionUpdates: AlternativeLowDefinitionPlayerUpdates,
    val movementStepsUpdates: MovementStepsPlayerUpdates
) : Packet

package xlitekt.game.packet

import xlitekt.game.actor.player.Viewport
import xlitekt.game.tick.NPCMovementStepsUpdates

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val npcMovementStepsUpdates: NPCMovementStepsUpdates
) : Packet

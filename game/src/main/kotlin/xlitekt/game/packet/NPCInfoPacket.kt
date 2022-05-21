package xlitekt.game.packet

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Viewport

/**
 * @author Jordan Abraham
 */
data class NPCInfoPacket(
    val viewport: Viewport,
    val steps: Map<NPC, MovementStep>
) : Packet

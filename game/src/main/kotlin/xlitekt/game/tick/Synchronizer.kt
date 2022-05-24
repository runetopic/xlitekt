package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()

    protected fun Player.sync(
        players: Map<Int, Player>,
        highDefinitionUpdates: Map<Player, ByteArray>,
        lowDefinitionUpdates: Map<Player, ByteArray>,
        playerSteps: Map<Player, MovementStep?>,
        npcSteps: Map<NPC, MovementStep>
    ) {
        write(PlayerInfoPacket(players, viewport, highDefinitionUpdates, lowDefinitionUpdates, playerSteps))
        write(NPCInfoPacket(viewport, npcSteps))
        flushPool()
        postSync()
    }
}

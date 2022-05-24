package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.Optional

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()

    protected fun Player.sync(
        players: Map<Int, Player>,
        highDefinitionUpdates: Map<Player, Optional<ByteArray>>,
        lowDefinitionUpdates: Map<Player, Optional<ByteArray>>,
        playerSteps: Map<Player, Optional<MovementStep>>,
        npcSteps: Map<NPC, Optional<MovementStep>>
    ) {
        write(PlayerInfoPacket(players, viewport, highDefinitionUpdates, lowDefinitionUpdates, playerSteps))
        write(NPCInfoPacket(viewport, npcSteps))
        flushPool()
        postSync()
    }
}

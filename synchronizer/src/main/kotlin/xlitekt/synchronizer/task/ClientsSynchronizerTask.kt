package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.synchronizer.builder.MovementUpdatesBuilder
import xlitekt.synchronizer.builder.NpcHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerAlternativeHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerAlternativeLowDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerHighDefinitionUpdatesBuilder
import xlitekt.synchronizer.builder.PlayerLowDefinitionUpdatesBuilder

/**
 * @author Jordan Abraham
 */
class ClientsSynchronizerTask(
    private val playerMovementUpdatesBuilder: MovementUpdatesBuilder<Player>,
    private val playerHighDefinitionUpdatesBuilder: PlayerHighDefinitionUpdatesBuilder,
    private val playerLowDefinitionUpdatesBuilder: PlayerLowDefinitionUpdatesBuilder,
    private val playerAlternativeHighDefinitionUpdatesBuilder: PlayerAlternativeHighDefinitionUpdatesBuilder,
    private val playerAlternativeLowDefinitionUpdatesBuilder: PlayerAlternativeLowDefinitionUpdatesBuilder,
    private val npcHighDefinitionUpdatesBuilder: NpcHighDefinitionUpdatesBuilder,
    private val npcMovementUpdatesBuilder: MovementUpdatesBuilder<NPC>
) : SynchronizerTask<Player> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        actors.parallelStream().forEach {
            it.write(
                PlayerInfoPacket(
                    players = syncPlayers,
                    viewport = it.viewport,
                    highDefinitionUpdates = playerHighDefinitionUpdatesBuilder.get(),
                    lowDefinitionUpdates = playerLowDefinitionUpdatesBuilder.get(),
                    alternativeHighDefinitionUpdates = playerAlternativeHighDefinitionUpdatesBuilder.get(),
                    alternativeLowDefinitionUpdates = playerAlternativeLowDefinitionUpdatesBuilder.get(),
                    movementStepsUpdates = playerMovementUpdatesBuilder.get()
                )
            )
            it.write(
                NPCInfoPacket(
                    viewport = it.viewport,
                    highDefinitionUpdates = npcHighDefinitionUpdatesBuilder.get(),
                    movementStepsUpdates = npcMovementUpdatesBuilder.get()
                )
            )
            it.invokeAndClearWritePool()
            it.resetDefinitionRenderingBlocks()
        }
    }

    override fun finish() {
        playerMovementUpdatesBuilder.reset()
        playerHighDefinitionUpdatesBuilder.reset()
        playerLowDefinitionUpdatesBuilder.reset()
        playerAlternativeHighDefinitionUpdatesBuilder.reset()
        playerAlternativeLowDefinitionUpdatesBuilder.reset()
        npcHighDefinitionUpdatesBuilder.reset()
        npcMovementUpdatesBuilder.reset()
    }
}

package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.synchronizer.builder.UpdatesBuilder

/**
 * @author Jordan Abraham
 */
class NpcsBuildersTask(
    private val builders: List<UpdatesBuilder<NPC, *>>
) : SynchronizerTask<NPC> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<NPC>) {
        actors.parallelStream().forEach {
            builders.forEach { builder ->
                builder.build(syncPlayers, it)
            }
        }
    }

    override fun finish() {}
}

package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.synchronizer.builder.UpdatesBuilder

/**
 * @author Jordan Abraham
 */
class PlayersBuildersTask(
    private val builders: List<UpdatesBuilder<Player, *>>
) : SynchronizerTask<Player> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        actors.parallelStream().forEach {
            builders.forEach { builder ->
                builder.build(syncPlayers, it)
            }
        }
    }

    override fun finish() {}
}

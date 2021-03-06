package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.process

/**
 * @author Jordan Abraham
 */
class PlayersSynchronizerTask : SynchronizerTask<Player> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        actors.parallelStream().forEach {
            it.invokeAndClearReadPool()
            it.process()
        }
    }

    override fun finish() {}
}

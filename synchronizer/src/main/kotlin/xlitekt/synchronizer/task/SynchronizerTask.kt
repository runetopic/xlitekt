package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
interface SynchronizerTask<A : Actor> {
    fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<A>)
    fun finish()
}

package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
interface UpdatesBuilder<in A : Actor, out T> {
    fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: A)
    fun get(): Array<out T>
    fun reset()
}

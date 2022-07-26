package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.Actor
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.processInteractions
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class MovementUpdatesBuilder<in A : Actor> : UpdatesBuilder<A, MovementStep?> {

    private val steps = arrayOfNulls<MovementStep?>(World.MAX_NPCS)

    override fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: A) {
        actor.processInteractions(syncPlayers)?.let { steps[actor.index] = it }
    }

    override fun get() = steps

    override fun reset() {
        steps.fill(null, 0)
    }
}

package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class LogoutsSynchronizerTask : SynchronizerTask<Player> {

    private val world by inject<World>()

    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        // We can process max players at a time because logout is not very expensive to do.
        world.logoutRequests.take(World.MAX_PLAYERS).onEach(Player::logout).also(world.logoutRequests::removeAll)
    }

    override fun finish() {}
}

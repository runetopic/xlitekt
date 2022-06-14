package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class LoginsSynchronizerTask : SynchronizerTask<Player> {

    private val world by inject<World>()

    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        // For now let's process 150 login requests at a time.
        world.loginRequests.entries.take(150).onEach {
            it.key.init(it.value, syncPlayers)
        }.also(world.loginRequests.entries::removeAll)
    }

    override fun finish() {}
}

package xlitekt.game.loop.sync

import xlitekt.game.actor.player.Player
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class WorldLogoutSynchronizer : Synchronizer() {
    override fun run() {
        // We can process max players at a time because logout is not very expensive to do.
        world.logoutRequests.take(World.MAX_PLAYERS).onEach(Player::logout).also(world.logoutRequests::removeAll)
    }
}

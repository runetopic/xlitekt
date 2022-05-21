package xlitekt.game.tick

import xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
class WorldLoginSynchronizer : Synchronizer() {
    override fun run() {
        val players = world.players().associateBy(Player::index)
        // For now let's process 150 login requests at a time.
        world.loginRequests.entries.take(150).onEach {
            it.key.init(it.value, players)
        }.also(world.loginRequests.entries::removeAll)
    }
}

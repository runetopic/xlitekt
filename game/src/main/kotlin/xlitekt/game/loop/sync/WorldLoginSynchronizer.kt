package xlitekt.game.loop.sync

/**
 * @author Jordan Abraham
 */
class WorldLoginSynchronizer : Synchronizer() {
    override fun run() {
        world.processLoginRequests()
    }
}

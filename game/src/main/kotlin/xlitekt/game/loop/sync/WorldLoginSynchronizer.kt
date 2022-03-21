package xlitekt.game.loop.sync

/**
 * @author Jordan Abraham
 */
class WorldLoginSynchronizer : Synchronizer() {
    override fun run() {
        // For now let's process 500 login requests at a time.
        world.loginRequests.entries.take(500).onEach {
            it.key.init(it.value)
        }.also(world.loginRequests.entries::removeAll)
    }
}

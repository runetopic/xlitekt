package xlitekt.game.tick

/**
 * @author Jordan Abraham
 */
class WorldLoginSynchronizer : Synchronizer() {
    override fun run() {
        // For now let's process 250 login requests at a time.
        world.loginRequests.entries.take(250).onEach {
            it.key.init(it.value)
        }.also(world.loginRequests.entries::removeAll)
    }
}

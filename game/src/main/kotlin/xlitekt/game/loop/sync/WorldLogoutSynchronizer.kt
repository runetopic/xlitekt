package xlitekt.game.loop.sync

/**
 * @author Jordan Abraham
 */
class WorldLogoutSynchronizer : Synchronizer() {
    override fun run() {
        world.processLogoutRequests()
    }
}

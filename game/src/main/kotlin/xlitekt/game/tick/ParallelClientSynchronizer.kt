package xlitekt.game.tick

import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ParallelClientSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val syncPlayers = world.playersMapped()

        players.parallelStream().forEach {
            it.prayer.process()
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        val npcs = world.npcs()

        npcs.parallelStream().forEach {
            it.prayer.process()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        players.parallelStream().forEach {
            it.syncZones()
            it.syncClient(syncPlayers)
        }

        ZoneUpdates.parallelStream().forEach(Zone::finalizeUpdateRequests)
    }
}

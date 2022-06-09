package xlitekt.game.tick

import xlitekt.game.actor.process
import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ParallelClientSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val syncPlayers = world.playersMapped()

        players.parallelStream().forEach {
            it.invokeAndClearReadPool()
            it.process()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        val npcs = world.npcs()

        npcs.parallelStream().forEach {
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

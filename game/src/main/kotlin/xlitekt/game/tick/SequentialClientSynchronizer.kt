package xlitekt.game.tick

import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class SequentialClientSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val syncPlayers = world.playersMapped()

        players.forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        val npcs = world.npcs()

        npcs.forEach {
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        players.forEach {
            it.syncZones()
            it.syncClient(syncPlayers)
        }

        ZoneUpdates.forEach(Zone::finalizeUpdateRequests)
    }
}

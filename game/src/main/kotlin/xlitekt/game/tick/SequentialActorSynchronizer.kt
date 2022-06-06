package xlitekt.game.tick

import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class SequentialActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val syncPlayers = world.playersMapped()

        players.forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        npcs.forEach {
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        players.associateWith { it.zones().filter(Zone::updating) }.onEach {
            it.value.forEach { zone ->
                zone.invokeUpdateRequests(it.key)
            }
        }.also { it.values.flatten().distinct().forEach(Zone::finalizeUpdateRequests) }

        players.forEach {
            it.syncClient(syncPlayers)
        }

        resetSynchronizer()
    }
}

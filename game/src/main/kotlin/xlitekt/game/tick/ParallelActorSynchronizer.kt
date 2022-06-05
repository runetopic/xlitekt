package xlitekt.game.tick

import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val syncPlayers = world.playersMapped()

        players.parallelStream().forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        npcs.parallelStream().forEach {
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        players.associateWith { it.zones().filter(Zone::updating) }.onEach {
            it.value.parallelStream().forEach { zone ->
                zone.invokeUpdateRequests(it.key)
            }
        }.also { it.values.flatten().distinct().parallelStream().forEach(Zone::finalizeUpdateRequests) }

        players.parallelStream().forEach {
            it.syncClient(syncPlayers)
        }

        resetSynchronizer()
    }
}

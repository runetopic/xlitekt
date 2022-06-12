package xlitekt.game.tick

import xlitekt.game.actor.player.process

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

        resetSynchronizer()
    }
}

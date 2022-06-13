package xlitekt.game.tick

import xlitekt.game.actor.player.process

/**
 * @author Jordan Abraham
 */
class SequentialClientSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val syncPlayers = world.playersMapped()

        players.forEach {
            it.invokeAndClearReadPool()
            it.process()
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

        resetSynchronizer()
    }
}

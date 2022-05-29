package xlitekt.game.tick

import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val zones = world.zonesUpdating()
        val syncPlayers = players.associateBy(Player::index)

        players.parallelStream().forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        npcs.parallelStream().forEach {
            it.syncMovement(syncPlayers)
        }

        zones.parallelStream().forEach(Zone::update)

        players.parallelStream().forEach {
            it.syncClient(syncPlayers)
        }

        resetSynchronizer()
    }
}

package xlitekt.game.tick

import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class SequentialActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val zones = players.flatMap(Player::zones).distinct().filter(Zone::updating)
        val syncPlayers = players.associateBy(Player::index)

        players.forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        npcs.forEach {
            it.syncMovement(syncPlayers)
        }

        zones.parallelStream().forEach(Zone::update)

        players.forEach {
            it.syncClient(syncPlayers)
        }

        resetSynchronizer()
    }
}

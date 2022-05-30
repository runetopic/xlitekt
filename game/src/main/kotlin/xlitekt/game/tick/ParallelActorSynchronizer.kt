package xlitekt.game.tick

import kotlin.random.Random
import xlitekt.game.actor.overheadChat
import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ParallelActorSynchronizer : Synchronizer() {

    override fun run() {
        val players = world.players()
        val npcs = world.npcs()
        val zones = players.flatMap(Player::zones).distinct().filter(Zone::updating)
        val syncPlayers = players.associateBy(Player::index)

        players.parallelStream().forEach {
            it.invokeAndClearReadPool()
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        npcs.parallelStream().forEach {
            it.syncMovement(syncPlayers)
            it.syncRenderingBlocks()
        }

        zones.parallelStream().forEach(Zone::update)

        players.parallelStream().forEach {
            it.syncClient(syncPlayers)
        }

        resetSynchronizer()
    }
}

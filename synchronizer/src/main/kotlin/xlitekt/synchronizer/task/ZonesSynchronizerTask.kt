package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import org.jctools.maps.NonBlockingHashSet
import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.zone.Zone

/**
 * @author Jordan Abraham
 */
class ZonesSynchronizerTask(
    private val zoneUpdates: NonBlockingHashSet<Zone>
) : SynchronizerTask<Player> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<Player>) {
        actors.parallelStream().forEach {
            it.zones().filter(Zone::updating).forEach { zone ->
                zoneUpdates.add(zone.invokeUpdateRequests(it))
            }
        }
    }

    override fun finish() {
        zoneUpdates.parallelStream().forEach(Zone::finalizeUpdateRequests)
        zoneUpdates.clear()
    }
}

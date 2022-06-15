package xlitekt.synchronizer.task

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player

/**
 * @author Jordan Abraham
 */
class NpcsSynchronizerTask : SynchronizerTask<NPC> {
    override fun execute(syncPlayers: NonBlockingHashMapLong<Player>, actors: List<NPC>) {}
    override fun finish() {}
}

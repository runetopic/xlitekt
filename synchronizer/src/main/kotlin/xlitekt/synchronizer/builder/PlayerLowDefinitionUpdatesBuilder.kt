package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeLowDefinitionPlayerRenderingBlocks
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class PlayerLowDefinitionUpdatesBuilder : UpdatesBuilder<Player, ByteArray?> {

    private val updates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)

    override fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: Player) {
        updates[actor.index] = actor.lowDefinitionRenderingBlocks().invokeLowDefinitionPlayerRenderingBlocks()
    }

    override fun get() = updates

    override fun reset() {
        updates.fill(null, 0)
    }
}

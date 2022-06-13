package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionPlayerRenderingBlocks
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class PlayerAlternativeLowDefinitionUpdatesBuilder : UpdatesBuilder<Player, ByteArray?> {

    private val updates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)

    override fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: Player) {
        updates[actor.index] = actor.alternativeLowDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks()
    }

    override fun get() = updates

    override fun reset() {
        updates.fill(null, 0)
    }
}

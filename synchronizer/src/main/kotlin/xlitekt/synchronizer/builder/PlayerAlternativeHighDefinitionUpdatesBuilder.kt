package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeAlternativeDefinitionPlayerRenderingBlocks
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class PlayerAlternativeHighDefinitionUpdatesBuilder : UpdatesBuilder<Player, ByteArray?> {

    private val updates = arrayOfNulls<ByteArray?>(World.MAX_PLAYERS)

    override fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: Player) {
        val highDefinition = actor.highDefinitionRenderingBlocks()
        if (highDefinition.isNotEmpty()) updates[actor.index] = actor.alternativeHighDefinitionRenderingBlocks().invokeAlternativeDefinitionPlayerRenderingBlocks()
    }

    override fun get() = updates

    override fun reset() {
        updates.fill(null, 0)
    }
}

package xlitekt.synchronizer.builder

import org.jctools.maps.NonBlockingHashMapLong
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.invokeHighDefinitionNPCRenderingBlocks
import xlitekt.game.world.World

/**
 * @author Jordan Abraham
 */
class NpcHighDefinitionUpdatesBuilder : UpdatesBuilder<NPC, ByteArray?> {

    private val updates = arrayOfNulls<ByteArray?>(World.MAX_NPCS)

    override fun build(syncPlayers: NonBlockingHashMapLong<Player>, actor: NPC) {
        val blocks = actor.highDefinitionRenderingBlocks()
        if (blocks.isEmpty()) return
        updates[actor.index] = blocks.invokeHighDefinitionNPCRenderingBlocks()
        actor.resetDefinitionRenderingBlocks()
    }

    override fun get() = updates

    override fun reset() {
        updates.fill(null, 0)
    }
}

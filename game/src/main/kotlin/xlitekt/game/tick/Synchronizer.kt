package xlitekt.game.tick

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    val world by inject<World>()

    protected fun Player.processUpdateBlocks(pending: List<Render>): ByteReadPacket {
        if (pending.isEmpty()) return ByteReadPacket.Empty
        return pending.buildPlayerUpdateBlocks(this)
    }

    protected fun Player.sync(
        players: Map<Int, Player>,
        pendingUpdates: Map<Player, ByteReadPacket>,
        cachedUpdates: Map<Player, ByteArray>,
        playerSteps: Map<Player, MovementStep?>,
        npcSteps: Map<NPC, MovementStep>
    ) {
        write(PlayerInfoPacket(players, viewport, pendingUpdates, cachedUpdates, playerSteps))
        write(NPCInfoPacket(viewport, npcSteps))
        flushPool()
        postSync()
    }
}

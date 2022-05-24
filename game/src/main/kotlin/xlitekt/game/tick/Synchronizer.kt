package xlitekt.game.tick

import xlitekt.game.actor.movement.MovementStep
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.Optional
import java.util.concurrent.ConcurrentHashMap

/**
 * @author Jordan Abraham
 */
abstract class Synchronizer : Runnable {

    protected val world by inject<World>()
    protected val playerMovementStepsUpdates = PlayerMovementStepsUpdates()
    protected val npcMovementStepsUpdates = NPCMovementStepsUpdates()
    protected val highDefinitionUpdates = HighDefinitionUpdates()
    protected val lowDefinitionUpdates = LowDefinitionUpdates()

    protected fun Player.sync(players: Map<Int, Player>) {
        write(PlayerInfoPacket(players, viewport, highDefinitionUpdates, lowDefinitionUpdates, playerMovementStepsUpdates))
        write(NPCInfoPacket(viewport, npcMovementStepsUpdates))
        flushPool()
        postSync()
    }

    protected fun resetSynchronizer() {
        playerMovementStepsUpdates.clear()
        npcMovementStepsUpdates.clear()
        highDefinitionUpdates.clear()
        lowDefinitionUpdates.clear()
    }
}

class HighDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap<Int, Optional<ByteArray>>(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() {
        updates.clear()
    }
}

class LowDefinitionUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<ByteArray>> = ConcurrentHashMap<Int, Optional<ByteArray>>(World.MAX_PLAYERS)
) : Map<Int, Optional<ByteArray>> by updates {
    internal fun add(player: Player, bytes: ByteArray?) {
        updates[player.index] = Optional.ofNullable(bytes)
    }

    internal fun clear() {
        updates.clear()
    }
}

class PlayerMovementStepsUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<MovementStep>> = ConcurrentHashMap<Int, Optional<MovementStep>>(World.MAX_PLAYERS)
) : Map<Int, Optional<MovementStep>> by updates {
    internal fun add(player: Player, movementStep: MovementStep?) {
        updates[player.index] = Optional.ofNullable(movementStep)
    }

    internal fun clear() {
        updates.clear()
    }
}

class NPCMovementStepsUpdates(
    private val updates: ConcurrentHashMap<Int, Optional<MovementStep>> = ConcurrentHashMap<Int, Optional<MovementStep>>()
) : Map<Int, Optional<MovementStep>> by updates {
    internal fun add(npc: NPC, movementStep: MovementStep?) {
        updates[npc.index] = Optional.ofNullable(movementStep)
    }

    internal fun clear() {
        updates.clear()
    }
}

package xlitekt.game.queue

import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player

/**
 * @author Tyler Telis
 */
class ActorScriptQueue : QueuedScriptList<Actor>() {

    /**
     * This will process all the queued script items and handle the suspension and priority accordingly.
     */
    override fun process() {
        while (queue.isNotEmpty()) {
            val script = queue.peekFirst() ?: break

            if (script.priority == QueuedScriptPriority.Normal && script.executor is Player && script.executor.interfaces.modalOpen()) break

            if (!script.executed) script.execute()

            script.process()

            if (!script.suspended()) {
                queue.remove(script)
                continue
            }

            break
        }
    }
}

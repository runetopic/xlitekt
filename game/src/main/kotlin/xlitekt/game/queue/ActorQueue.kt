package xlitekt.game.queue

import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

open class ActorQueue<T>(
    val actor: T,
    private val queue: ConcurrentLinkedQueue<QueueScript<T>> = ConcurrentLinkedQueue<QueueScript<T>>()
) : Queue<QueueScript<T>> by queue {

    fun process(): Int {
        var count = 0

        queue.removeIf { script ->
            if (actor is Player && (script.priority == QueuePriority.Strong || script.priority == QueuePriority.Soft)) actor.interfaces.closeModal()

            if (script.shouldProcess()) {
                script.process()
                count++
                return@removeIf true
            }

            return@removeIf false
        }

        return count
    }

    fun weak(function: (T).() -> Unit) {
        queue.add(
            QueueScript(
                priority = QueuePriority.Weak,
                callback = function,
                actor = actor
            )
        )
    }

    fun normal(function: (T).() -> Unit) {
        queue.add(
            QueueScript(
                priority = QueuePriority.Normal,
                callback = function,
                actor = actor
            )
        )
    }

    fun strong(function: (T).() -> Unit) {
        queue.add(
            QueueScript(
                priority = QueuePriority.Strong,
                callback = function,
                actor = actor
            )
        )
    }

    fun soft(function: (T).() -> Unit) {
        queue.add(
            QueueScript(
                priority = QueuePriority.Soft,
                callback = function,
                actor = actor
            )
        )
    }

    fun cancelWeak() {
        queue.removeIf { script ->
            script.priority == QueuePriority.Weak
        }
    }
}

typealias PlayerQueue = ActorQueue<Player>
typealias NPCQueue = ActorQueue<NPC>

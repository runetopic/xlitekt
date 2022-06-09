package xlitekt.game.queue

import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Player
import java.util.Queue
import java.util.concurrent.ConcurrentLinkedQueue

data class QueueScript<T>(
    val priority: QueuePriority,
    val callback: (T).() -> Unit,
    val actor: T
) {
    fun future(): Boolean {
        return false // TODO: scripts set to execute in the future
    }

    fun process() {
        callback.invoke(actor)
    }
}

fun <T> QueueScript<T>.shouldProcess(): Boolean = priority == QueuePriority.Soft || this.actor is Player && !this.actor.interfaces.modalOpen() && !future()

open class ActorQueue<T>(
    val actor: T,
    private val queue: ConcurrentLinkedQueue<QueueScript<T>> = ConcurrentLinkedQueue<QueueScript<T>>()
) : Queue<QueueScript<T>> by queue {

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

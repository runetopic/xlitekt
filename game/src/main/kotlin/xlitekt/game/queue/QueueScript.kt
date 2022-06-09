package xlitekt.game.queue

import xlitekt.game.actor.player.Player

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

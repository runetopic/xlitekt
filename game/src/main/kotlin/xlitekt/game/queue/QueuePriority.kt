package xlitekt.game.queue

sealed class QueuePriority {
    object Weak : QueuePriority()
    object Normal : QueuePriority()
    object Strong : QueuePriority()
    object Soft : QueuePriority()
}

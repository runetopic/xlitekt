package xlitekt.game.queue

/**
 * This class represents all the possible queue priority types.
 * @author Tyler Telis
 */
sealed class QueuedScriptPriority {
    object Weak : QueuedScriptPriority()
    object Normal : QueuedScriptPriority()
    object Strong : QueuedScriptPriority()
    object Soft : QueuedScriptPriority()
}

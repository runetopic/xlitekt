package xlitekt.game.queue

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.asCoroutineDispatcher
import xlitekt.game.actor.Actor
import java.util.LinkedList
import java.util.concurrent.Executors
import kotlin.coroutines.createCoroutine

// TODO: this might need to go in it's own file to be shared across the project.
val scriptDispatcher = Executors.newSingleThreadExecutor().asCoroutineDispatcher()

/**
 * This is a base class used for storing all the queued scripts for Actors.
 * @author Tyler Telis
 */
abstract class QueuedScriptList<T : Actor>(
    protected val queue: LinkedList<QueuedScript<T>> = LinkedList()
) : List<QueuedScript<T>> by queue {
    abstract fun process(actor: T)

    /**
     * This function queues a script up in the script list.
     * @param executor The executor of the script
     * @param priority The priority of the script.
     * @param suspendedScript The suspendable queued script function to invoke.
     */
    fun queue(executor: T, priority: QueuedScriptPriority, suspendedScript: SuspendableQueuedScript<T>) {
        val task = QueuedScript(executor, priority)
        val suspendBlock = suspend { suspendedScript(task, CoroutineScope(scriptDispatcher)) }

        task.continuation = suspendBlock.createCoroutine(completion = task)

        if (priority == QueuedScriptPriority.Strong) cancelWeakScripts()

        queue.addFirst(task)
    }

    /**
     * Takes the last input from the first item in the queue.
     * This does not remove the item from the queue.
     */
    fun takeInput(value: Any) {
        val task = queue.peek() ?: return
        task.input = value
    }

    /**
     * Removes all weak scripts in the queue and terminates them.
     */
    fun cancelWeakScripts() {
        queue.removeIf {
            if (it.priority == QueuedScriptPriority.Weak) {
                it.terminate()
                return@removeIf true
            }

            return@removeIf false
        }
    }

    /**
     * Removes all the scripts in the queue and terminates them.
     */
    fun cancelAllScripts() {
        queue.removeAll {
            it.terminate()
            return@removeAll true
        }
    }
}

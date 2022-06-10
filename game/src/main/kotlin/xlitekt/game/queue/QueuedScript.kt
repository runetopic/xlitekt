package xlitekt.game.queue

import com.github.michaelbull.logging.InlineLogger
import kotlinx.coroutines.CoroutineScope
import xlitekt.game.actor.Actor
import xlitekt.game.world.map.Location
import kotlin.coroutines.Continuation
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * @author Tyler Telis
 * @author Tom (Got some inspiration and coroutine ideas from rsmod)
 */
data class QueuedScript<T : Actor>(
    private val executor: T,
    val priority: QueuedScriptPriority,
) : Continuation<Unit> {
    /**
     * This is the coroutine's context, which starts off as an empty context for each script.
     */
    override val context: CoroutineContext = EmptyCoroutineContext

    /**
     * Inline logger for debugging.
     */
    private val logger = InlineLogger()

    /**
     * The continuation unit to invoke whe we need to resume this script.
     */
    lateinit var continuation: Continuation<Unit>

    /**
     * The current stage of the queued script.
     */
    private var stage: QueuedScriptStage? = null

    /**
     * If the script has been exectuted or not.
     */
    var executed = false

    /**
     * The termination script to exectute upon terminating the queued script.
     */
    var terminationScript: QueuedScriptUnit<T>? = null

    /**
     * Any input value that the script has been waiting for
     */
    var input: Any? = null

    /**
     * The function that gets executed when we resume the queued script with the result.
     * We set the current stage to null to allow for continuation.
     * If an exception is caught within the result, we print it to the console.
     */
    override fun resumeWith(result: Result<Unit>) {
        stage = null
        result.exceptionOrNull()?.let { e -> logger.error(e) { "There was a problem resuming with the result." } }
    }

    /**
     * This function processes the queued script, and resumes any execution if needed.
     */
    internal fun process() {
        val currentStage = stage ?: return

        if (currentStage.condition.resume()) {
            currentStage.continuation.resume(Unit)
            input = null
        }
    }

    /**
     * Marks the script as executed and resumes our execution.
     */
    fun execute() {
        executed = true
        continuation.resume(Unit)
    }

    /**
     * Terminates the queued script and invokes the termination script.
     */
    fun terminate() {
        stage = null
        input = null
        terminationScript?.invoke(this)
    }

    /**
     * Returns if the queued script is suspended.
     * E.g: the script is suspended when the stage is null
     */
    fun suspended(): Boolean = stage != null

    /**
     * This function sets the current stage to a WaitCondition to allow the script to wait for a specified number of ticks.
     */
    suspend fun wait(ticks: Int): Unit = suspendCoroutine {
        check(ticks > 0) { "You must provide at least 1 tick to suspend for." }
        stage = QueuedScriptStage(QueuedScriptConditions.WaitCondition(ticks), it)
    }

    /**
     * This function sets the current stage to a PredicateCondition to allow the script to wait until the predicate matches.
     */
    suspend fun waitUntil(predicate: () -> Boolean): Unit = suspendCoroutine {
        stage = QueuedScriptStage(QueuedScriptConditions.PredicateCondition { predicate() }, it)
    }

    /**
     * This function sets the current stage to a LocationCondition to allow the script to wait until the location matches.
     */
    suspend fun waitForLocation(location: Location): Unit = suspendCoroutine {
        stage = QueuedScriptStage(QueuedScriptConditions.LocationCondition(executor.location, location), it)
    }

    /**
     * This function sets the current stage to a PredicateCondition, that checks if the input is not null and waits for it.
     * This is used for dialogues/input dialogues that require waiting for user input.
     */
    suspend fun waitForInput(): Unit = suspendCoroutine {
        stage = QueuedScriptStage(QueuedScriptConditions.PredicateCondition { input != null }, it)
    }
}

typealias SuspendableQueuedScript<T> = suspend QueuedScript<T>.(CoroutineScope) -> Unit
typealias QueuedScriptUnit<T> = (QueuedScript<T>).() -> Unit

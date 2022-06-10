package xlitekt.game.queue

import kotlin.coroutines.Continuation

/**
 * This class represents the queued scripts current stage.
 * @author Tyler Telis
 */
data class QueuedScriptStage(
    val condition: QueuedScriptConditions,
    val continuation: Continuation<Unit>
)

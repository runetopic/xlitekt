package xlitekt.game.queue

import xlitekt.game.world.map.Location
import java.util.concurrent.atomic.AtomicInteger

/**
 * This class represents the possible script conditions.
 * @author Tyler Telis
 */
sealed class QueuedScriptConditions {
    abstract fun resume(): Boolean

    class WaitCondition(ticks: Int) : QueuedScriptConditions() {
        private val ticks = AtomicInteger(ticks)

        override fun resume(): Boolean = ticks.decrementAndGet() <= 0
    }

    class LocationCondition(private val location: Location, private val destination: Location) : QueuedScriptConditions() {
        override fun resume(): Boolean = location == destination
    }

    class PredicateCondition(private val predicate: () -> Boolean) : QueuedScriptConditions() {
        override fun resume(): Boolean = predicate.invoke()
    }
}

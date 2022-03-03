package xlitekt.game.event

import kotlin.reflect.KClass

/**
 * @author Tyler Telis
 */
class EventBus {
    val events: MutableMap<KClass<out Event>, MutableList<EventListener<*>>> = mutableMapOf()

    inline fun <reified T : Event> notify(event: T): Boolean {
        val eventListener = events[event::class] ?: return false

        return eventListener
            .asSequence()
            .filterIsInstance<EventListener<T>>()
            .filter { it.filter(event) }
            .map { it.use(event) }
            .count() > 0
    }

    inline fun <reified T : Event> onEvent(): EventBuilder<T> = EventBuilder(events.computeIfAbsent(T::class) { mutableListOf() })
}

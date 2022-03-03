package xlitekt.game.event

/**
 * @author Tyler Telis
 */
class EventBuilder<T : Event>(
    private val events: MutableList<EventListener<*>>
) {
    private var filter: (T).() -> Boolean = { true }

    fun filter(predicate: (T).() -> Boolean): EventBuilder<T> {
        this.filter = predicate
        return this
    }

    fun use(use: (T).() -> Unit) {
        events.add(EventListener(use, filter))
    }
}

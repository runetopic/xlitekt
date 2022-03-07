package xlitekt.game.event

import xlitekt.shared.inject

inline fun <reified T : Event> onEvent(noinline function: T.() -> Unit) {
    inject<EventBus>().value.onEvent<T>().use(function)
}

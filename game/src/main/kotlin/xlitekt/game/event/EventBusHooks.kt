package xlitekt.game.event

import xlitekt.shared.inject

inline fun <reified T : Event> onEvent(): EventBuilder<T> = inject<EventBus>().value.onEvent()

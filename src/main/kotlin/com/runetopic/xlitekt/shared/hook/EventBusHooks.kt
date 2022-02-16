package com.runetopic.xlitekt.shared.hook

import com.runetopic.xlitekt.game.event.Event
import com.runetopic.xlitekt.game.event.EventBuilder
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.plugin.koin.inject

inline fun <reified T : Event> onEvent(): EventBuilder<T> = inject<EventBus>().value.onEvent()

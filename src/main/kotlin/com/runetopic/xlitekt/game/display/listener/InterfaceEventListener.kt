package com.runetopic.xlitekt.game.display.listener

import com.runetopic.xlitekt.game.event.impl.IfButtonClickEvent
import com.runetopic.xlitekt.game.event.impl.IfOpenSubEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 */
class InterfaceEventListener(
    private val interfaceId: Int
) {
    fun onClick(function: (IfButtonClickEvent).() -> Unit) =
        onEvent<IfButtonClickEvent>()
            .filter { interfaceId == this@InterfaceEventListener.interfaceId }
            .use { function.invoke(this) }

    fun onOpenSub(function: (IfOpenSubEvent).() -> Unit) =
        onEvent<IfOpenSubEvent>()
            .filter { interfaceId == this@InterfaceEventListener.interfaceId }
            .use { function.invoke(this) }

    companion object {
        fun addInterfaceListener(interfaceId: Int, function: InterfaceEventListener.() -> Unit) {
            function.invoke(InterfaceEventListener(interfaceId))
        }
    }
}

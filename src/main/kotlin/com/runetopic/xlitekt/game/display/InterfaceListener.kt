package com.runetopic.xlitekt.game.display

import com.runetopic.xlitekt.game.event.impl.IfButtonClickEvent
import com.runetopic.xlitekt.game.event.impl.IfOpenEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 */
class InterfaceListener(
    private val interfaceId: Int
) {
    fun onClick(function: (IfButtonClickEvent).() -> Unit) =
        onEvent<IfButtonClickEvent>()
            .filter { interfaceId == this@InterfaceListener.interfaceId }
            .use { function.invoke(this) }

    fun onOpen(function: (IfOpenEvent).() -> Unit) =
        onEvent<IfOpenEvent>()
            .filter { interfaceId == this@InterfaceListener.interfaceId }
            .use { function.invoke(this) }

    companion object {
        fun addInterfaceListener(interfaceId: Int, function: InterfaceListener.() -> Unit) {
            function.invoke(InterfaceListener(interfaceId))
        }
    }
}

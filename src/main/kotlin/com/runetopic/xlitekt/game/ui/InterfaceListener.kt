package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.event.impl.IfButtonClickEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 */
class InterfaceListener(
    private val event: IfButtonClickEvent
) {
    fun onClick(function: (IfButtonClickEvent).() -> Unit) = function.invoke(event)

    fun onClick(childId: Int, function: (IfButtonClickEvent).() -> Unit) {
        if (event.childId == childId) {
            function.invoke(event)
        }
    }

    fun onOption(option: String, function: (IfButtonClickEvent).() -> Unit) {
        if (event.option == option) {
            function.invoke(event)
        }
    }

    companion object {
        fun addInterfaceListener(interfaceId: Int, function: InterfaceListener.() -> Unit) {
            onEvent<IfButtonClickEvent>().filter { this.interfaceId == interfaceId }.use {
                function.invoke(InterfaceListener(this))
            }
        }
    }
}

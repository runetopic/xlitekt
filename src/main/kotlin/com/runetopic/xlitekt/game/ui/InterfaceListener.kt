package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 */
class InterfaceListener(
    private val event: IfEvent
) {
    fun onOpenTop(function: (IfEvent.IfOpenTopEvent).() -> Unit) {
        if (event !is IfEvent.IfOpenTopEvent) return
        function.invoke(event)
    }

    fun onOpenSub(function: (IfEvent.IfOpenSubEvent).() -> Unit) {
        if (event !is IfEvent.IfOpenSubEvent) return
        function.invoke(event)
    }

    fun onClick(function: (IfEvent.IfButtonClickEvent).() -> Unit) {
        if (event !is IfEvent.IfButtonClickEvent) return
        function.invoke(event)
    }

    fun onClick(childId: Int, function: (IfEvent.IfButtonClickEvent).() -> Unit) {
        if (event !is IfEvent.IfButtonClickEvent || event.childId != childId) return
        function.invoke(event)
    }

    fun onOption(option: String, function: (IfEvent.IfButtonClickEvent).() -> Unit) {
        if (event !is IfEvent.IfButtonClickEvent || event.option != option) return
        function.invoke(event)
    }

    companion object {
        inline fun addInterfaceListener(interfaceId: Int, crossinline function: InterfaceListener.() -> Unit) {
            onEvent<IfEvent.IfButtonClickEvent>().filter { this.interfaceId == interfaceId }.use {
                function.invoke(InterfaceListener(this))
            }
            onEvent<IfEvent.IfOpenTopEvent>().filter { this.interfaceId == interfaceId }.use {
                function.invoke(InterfaceListener(this))
            }
            onEvent<IfEvent.IfOpenSubEvent>().filter { this.interfaceId == interfaceId }.use {
                function.invoke(InterfaceListener(this))
            }
        }
    }
}

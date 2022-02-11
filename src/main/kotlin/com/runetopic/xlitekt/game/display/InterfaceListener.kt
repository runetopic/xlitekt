package com.runetopic.xlitekt.game.display

import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 */
class InterfaceListener(
    private val interfaceId: Int
) {
    fun onClick(function: (IfEvent.IfButtonClickEvent).() -> Unit) =
        onEvent<IfEvent.IfButtonClickEvent>()
            .filter { interfaceId == this@InterfaceListener.interfaceId }
            .use { function.invoke(this) }

    fun onClick(childId: Int, function: (IfEvent.IfButtonClickEvent).() -> Unit) =
        onEvent<IfEvent.IfButtonClickEvent>()
            .filter { this.interfaceId == this@InterfaceListener.interfaceId }
            .filter { this.childId == childId }
            .use { function.invoke(this) }

    fun onOpen(function: (IfEvent.IfOpenEvent).() -> Unit) =
        onEvent<IfEvent.IfOpenEvent>()
            .filter { interfaceId == this@InterfaceListener.interfaceId }
            .use { function.invoke(this) }

    fun IfEvent.event(childId: Int, slots: IntRange, events: InterfaceEvent) = player.interfaceManager.apply {
        interfaceEvents(
            interfaceId,
            childId = childId,
            fromSlot = slots.first,
            toSlot = slots.last,
            events = events
        )
    }

    fun IfEvent.sendVarBit(id: Int, value: Int) = player.varsManager.sendVarBit(id, value)
    fun IfEvent.runClientScript(scriptId: Int, parameters: List<Any>) = player.interfaceManager.apply { clientScript(scriptId, parameters) }

    companion object {
        fun buildInterfaceListener(interfaceId: Int, function: InterfaceListener.() -> Unit) = function.invoke(InterfaceListener(interfaceId))
    }
}

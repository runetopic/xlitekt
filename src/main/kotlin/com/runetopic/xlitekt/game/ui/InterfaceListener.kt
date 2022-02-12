package com.runetopic.xlitekt.game.ui

import com.runetopic.xlitekt.game.event.impl.IfEvent
import com.runetopic.xlitekt.util.hook.onEvent

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
@JvmInline
value class InterfaceListener(
    val interfaceId: Int
) {
    inline fun onClick(crossinline function: (IfEvent.IfButtonClickEvent).() -> Unit) =
        onEvent<IfEvent.IfButtonClickEvent>()
            .filter { interfaceId == this@InterfaceListener.interfaceId }
            .use { function.invoke(this) }

    inline fun onClick(childId: Int, crossinline function: (IfEvent.IfButtonClickEvent).() -> Unit) =
        onEvent<IfEvent.IfButtonClickEvent>()
            .filter { this.interfaceId == this@InterfaceListener.interfaceId }
            .filter { this.childId == childId }
            .use { function.invoke(this) }

    inline fun onOpen(crossinline function: (IfEvent.IfOpenEvent).() -> Unit) =
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
        inline fun buildInterfaceListener(interfaceId: Int, function: InterfaceListener.() -> Unit) = function.invoke(InterfaceListener(interfaceId))
    }
}

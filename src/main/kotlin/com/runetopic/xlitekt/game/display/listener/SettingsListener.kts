package com.runetopic.xlitekt.game.display.listener

import com.runetopic.xlitekt.game.display.InterfaceId
import com.runetopic.xlitekt.game.display.InterfaceListener.Companion.addInterfaceListener
import com.runetopic.xlitekt.game.display.Layout

private val layoutDropDownChildId = 84

addInterfaceListener(InterfaceId.SETTINGS) {
    onOpen {
        player.interfaceManager.let { interfaceManager ->
            interfaceManager.interfaceEvents(interfaceId, childId = 41, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 55, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 69, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 81, fromSlot = 1, toSlot = 5, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 82, fromSlot = 1, toSlot = 4, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 84, fromSlot = 1, toSlot = 3, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 23, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId, childId = 83, fromSlot = 1, toSlot = 5, events = 2)
        }
    }

    onClick(layoutDropDownChildId) {
        player.interfaceManager.let { interfaceManager ->
            val mode = Layout.values().firstOrNull { it.id == slotId - 1 } ?: return@let
            if (mode == Layout.FIXED || mode == Layout.RESIZABLE) {
                interfaceManager.clientScript(3998, listOf(mode.id))
            }
            player.interfaceManager.switchLayout(mode)
        }
    }
}

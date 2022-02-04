package com.runetopic.xlitekt.game.display.listener.impl

import com.runetopic.xlitekt.game.display.ClientLayout
import com.runetopic.xlitekt.game.display.InterfaceId
import com.runetopic.xlitekt.game.display.listener.InterfaceEventListener.Companion.addInterfaceListener

// TODO this will be a kts file when i add kotlin script plugins
val optionsListener = addInterfaceListener(InterfaceId.OPTIONS) {
    onOpenSub {
        player.interfaceManager.let { interfaceManager ->
            interfaceManager.interfaceEvents(interfaceId = 216, childId = 1, fromSlot = 0, toSlot = 50, events = 6)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 41, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 55, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 69, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 81, fromSlot = 1, toSlot = 5, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 82, fromSlot = 1, toSlot = 4, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 84, fromSlot = 1, toSlot = 3, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 23, fromSlot = 0, toSlot = 21, events = 2)
            interfaceManager.interfaceEvents(interfaceId = 116, childId = 83, fromSlot = 1, toSlot = 5, events = 2)
        }
    }

    onClick {
        player.interfaceManager.let { interfaceManager ->
            when (slotId) {
                1 -> interfaceManager.switchDisplayMode(ClientLayout.FIXED)
                2 -> interfaceManager.switchDisplayMode(ClientLayout.RESIZABLE)
                3 -> interfaceManager.switchDisplayMode(ClientLayout.RESIZABLE_LIST)
                else -> interfaceManager.message("Unhandled slotId = $slotId for options interface.")
            }
        }
    }
}

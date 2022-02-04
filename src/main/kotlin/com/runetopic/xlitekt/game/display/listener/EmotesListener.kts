package com.runetopic.xlitekt.game.display.listener

import com.runetopic.xlitekt.game.display.InterfaceId
import com.runetopic.xlitekt.game.display.InterfaceListener.Companion.addInterfaceListener

addInterfaceListener(InterfaceId.EMOTES) {
    onOpen {
        player.interfaceManager.interfaceEvents(interfaceId, childId = 1, fromSlot = 0, toSlot = 50, events = 6)
    }
}

package com.runetopic.xlitekt.game.display.listener

import com.runetopic.xlitekt.game.display.InterfaceEvent.ClickOp1
import com.runetopic.xlitekt.game.display.InterfaceId
import com.runetopic.xlitekt.game.display.InterfaceListener.Companion.buildInterfaceListener

buildInterfaceListener(InterfaceId.EMOTES) {
    onOpen {
        event(childId = 1, slots = 0..50, events = ClickOp1)
    }
}

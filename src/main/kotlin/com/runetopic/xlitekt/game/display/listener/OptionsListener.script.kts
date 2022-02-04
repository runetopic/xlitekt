import com.runetopic.xlitekt.game.display.InterfaceId
import com.runetopic.xlitekt.game.display.Layout
import com.runetopic.xlitekt.game.display.InterfaceListener.Companion.addInterfaceListener

addInterfaceListener(InterfaceId.OPTIONS) {
    val FIXED_CHILD_ID = 1
    val RESIZABLE_CHILD_ID = 2
    val RESIZABNLE_LIST_CHILD_ID = 3

    onOpen {
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
                FIXED_CHILD_ID -> interfaceManager.switchLayout(Layout.FIXED)
                RESIZABLE_CHILD_ID -> interfaceManager.switchLayout(Layout.RESIZABLE)
                RESIZABNLE_LIST_CHILD_ID -> interfaceManager.switchLayout(Layout.RESIZABLE_LIST)
                else -> interfaceManager.message("Unhandled slotId = $slotId")
            }
        }
    }
}

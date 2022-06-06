package script.ui

import xlitekt.game.content.ui.InterfaceEvent
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface

/**
 * @author Justin Kenney
 */
onInterface<UserInterface.Prayer> {
    onOpen {
        setEvent(interfaceId = 541, childIds = 5..33, slot = 65535, event = InterfaceEvent.CLICK_OPTION_1)
    }

    onClick {
        this.prayer.switch(it.childId)
    }
}

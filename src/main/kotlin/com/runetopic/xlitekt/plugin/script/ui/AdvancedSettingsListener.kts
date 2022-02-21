package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceEvent
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface


private val closeSettingsChildId = 4

/**
 * @author Jordan Abraham
 */
onInterface<UserInterface.AdvancedSettings> {
    onInit {
        // Varbit(id = 9657, value = 0)
        // Varbit(id = 9656, value = 4)
        // Varbit(id = 9658, value = 1)
    }
    onOpen {
        setEvent(21, 0..147, InterfaceEvent.CLICK_OPTION_1)
        setEvent(23, 0..7, InterfaceEvent.CLICK_OPTION_1)
        setEvent(19, 0..240, InterfaceEvent.CLICK_OPTION_1)
        setEvent(28, 0..122, InterfaceEvent.CLICK_OPTION_1)
    }

    onClick(childId = closeSettingsChildId) {
        interfaces -= UserInterface.AdvancedSettings
    }
}

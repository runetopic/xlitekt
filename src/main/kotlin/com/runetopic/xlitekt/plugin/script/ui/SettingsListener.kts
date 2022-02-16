package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceEvent.CLICK_OPTION_1
import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.game.ui.InterfaceMapping.buildInterfaceListener
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.shared.toInt

private val layoutDropDownChildId = 84
private val clientModeCS2Id = 3998
private val interfaceScalingCS2Id = 2358

buildInterfaceListener<UserInterface.Settings> {
    onOpen {
        setEvent(childId = 41, slots = 0..21, event = CLICK_OPTION_1)
        setEvent(childId = 55, slots = 0..21, event = CLICK_OPTION_1)
        setEvent(childId = 69, slots = 0..21, event = CLICK_OPTION_1)
        setEvent(childId = 81, slots = 1..5, event = CLICK_OPTION_1)
        setEvent(childId = 82, slots = 1..4, event = CLICK_OPTION_1)
        setEvent(childId = 84, slots = 1..3, event = CLICK_OPTION_1)
        setEvent(childId = 23, slots = 0..21, event = CLICK_OPTION_1)
        setEvent(childId = 83, slots = 1..5, event = CLICK_OPTION_1)
    }

    onClick(layoutDropDownChildId) {
        val interfaceLayout = enumValues<InterfaceLayout>().find { it.id == slotId } ?: return@onClick
        player.varsManager.sendVarBit(4607, (slotId == 3).toInt())
        player.interfaceManager.runClientScript(clientModeCS2Id, listOf(slotId - 1))
        player.interfaceManager.runClientScript(interfaceScalingCS2Id, listOf(0))
        player.interfaceManager.switchLayout(interfaceLayout)
    }
}

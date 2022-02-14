package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceEvent.CLICK_OPTION_1
import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.game.ui.InterfaceMapping.buildInterfaceListener
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.resource.VarBits

private val layoutDropDownChildId = 84
private val clientModeCS2Id = 3998
private val varBits by inject<VarBits>()

buildInterfaceListener<UserInterface.Settings> {
    onOpen {
        event(childId = 41, slots = 0..21, events = CLICK_OPTION_1)
        event(childId = 55, slots = 0..21, events = CLICK_OPTION_1)
        event(childId = 69, slots = 0..21, events = CLICK_OPTION_1)
        event(childId = 81, slots = 1..5, events = CLICK_OPTION_1)
        event(childId = 82, slots = 1..4, events = CLICK_OPTION_1)
        event(childId = 84, slots = 1..3, events = CLICK_OPTION_1)
        event(childId = 23, slots = 0..21, events = CLICK_OPTION_1)
        event(childId = 83, slots = 1..5, events = CLICK_OPTION_1)
    }

    onClick(layoutDropDownChildId) {
        val interfaceLayout = enumValues<InterfaceLayout>().find { it.id == slotId - 1 } ?: return@onClick
        val sideStonesArrangementVarBit = varBits["side_stones_arrangement"] ?: return@onClick

        when (interfaceLayout) {
            InterfaceLayout.FIXED, InterfaceLayout.RESIZABLE -> player.interfaceManager.runClientScript(clientModeCS2Id, listOf(interfaceLayout.id))
            // TODO set the resizable mode on login based on the varbit set.
        }
//        player.interfaceManager.switchLayout(layout) // TODO Redo this since ive rebuilt the system
    }
}

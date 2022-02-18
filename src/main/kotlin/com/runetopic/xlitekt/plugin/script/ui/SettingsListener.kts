package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.actor.player.script
import com.runetopic.xlitekt.game.ui.InterfaceEvent.CLICK_OPTION_1
import com.runetopic.xlitekt.game.ui.InterfaceLayout
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface
import com.runetopic.xlitekt.game.vars.VarBit
import com.runetopic.xlitekt.shared.toInt

private val layoutDropDownChildId = 84
private val clientModeCS2Id = 3998
private val interfaceScalingCS2Id = 2358

onInterface<UserInterface.Settings> {
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
        val interfaceLayout = enumValues<InterfaceLayout>().find { layout -> layout.id == it.slotId } ?: return@onClick
        vars.set(VarBit.SideStonesArrangement, (it.slotId == 3).toInt())
        script(clientModeCS2Id, listOf(it.slotId - 1))
        script(interfaceScalingCS2Id, listOf(0))
        interfaces.switchLayout(interfaceLayout)
    }
}

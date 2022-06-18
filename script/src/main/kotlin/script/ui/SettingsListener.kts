package script.ui

import xlitekt.game.actor.player.script
import xlitekt.game.actor.speed
import xlitekt.game.content.ui.InterfaceEvent.ClickOption1
import xlitekt.game.content.ui.InterfaceLayout
import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.Settings
import xlitekt.game.content.vars.VarBit
import xlitekt.game.content.vars.VarPlayer
import xlitekt.shared.insert
import xlitekt.shared.toInt

private val layoutDropDownChildId = 84
private val clientModeCS2Id = 3998
private val interfaceScalingCS2Id = 2358

/**
 * @author Tyler Telis
 */
insert<InterfaceListener>().userInterface<Settings> {
    onOpen {
        setEvent(childId = 41, slots = 0..21, ClickOption1)
        setEvent(childId = 55, slots = 0..21, ClickOption1)
        setEvent(childId = 69, slots = 0..21, ClickOption1)
        setEvent(childId = 81, slots = 1..5, ClickOption1)
        setEvent(childId = 82, slots = 1..4, ClickOption1)
        setEvent(childId = 84, slots = 1..3, ClickOption1)
        setEvent(childId = 23, slots = 0..21, ClickOption1)
        setEvent(childId = 83, slots = 1..5, ClickOption1)
    }

    onClick(layoutDropDownChildId) {
        val interfaceLayout = enumValues<InterfaceLayout>().find { layout -> layout.id == it.slotId } ?: return@onClick
        vars[VarBit.SideStonesArrangement] = (it.slotId == 3).toInt()
        script(clientModeCS2Id, it.slotId - 1)
        script(interfaceScalingCS2Id, 0)
        interfaces.switchLayout(interfaceLayout)
    }

    onClick("All Settings") {
        interfaces += UserInterface.AdvancedSettings
    }

    onClick("Toggle Run") {
        vars.flip { VarPlayer.ToggleRun }
        speed { VarPlayer.ToggleRun in vars }
    }
}

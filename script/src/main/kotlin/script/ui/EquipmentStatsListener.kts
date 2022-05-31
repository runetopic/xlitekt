package script.ui

import xlitekt.game.actor.player.script
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.EquipmentStats
import xlitekt.game.content.ui.onInterface

/**
 * @author Tyler Telis
 */
onInterface<EquipmentStats> {
    onCreate {
        script(917, -1, -1)
    }

    onOpen {
        setText(24, "Stab +")
        interfaces += UserInterface.EquipmentInventory
    }

    onClose {
        interfaces.closeInventory()
    }
}

package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.actor.player.script
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface

onInterface<UserInterface.EquipmentStats> {
    onInit {
        script(917, listOf(-1, -1))
    }

    onOpen {
        setText(24, "Stab +")
        interfaces += UserInterface.EquipmentInventory
    }

    onClose {
        interfaces.closeInventory()
    }
}

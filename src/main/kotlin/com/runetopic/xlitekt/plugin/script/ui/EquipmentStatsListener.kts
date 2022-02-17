package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface

onInterface<UserInterface.EquipmentStats> {
    player.interfaceManager.runClientScript(917, listOf(-1, -1))

    onOpen {
        setText(24, "Stab +")
        player.interfaceManager.openInventory(UserInterface.EquipmentInventory)
    }

    onClose {
        player.interfaceManager.closeInventory()
    }
}

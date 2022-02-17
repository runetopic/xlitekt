package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface

onInterface<UserInterface.EquipmentStats> {
    onOpen {
        setText(24, "Stab +")
    }
}

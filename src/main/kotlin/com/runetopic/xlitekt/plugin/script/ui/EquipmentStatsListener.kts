package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceMapping.buildInterfaceListener
import com.runetopic.xlitekt.game.ui.UserInterface

buildInterfaceListener<UserInterface.EquipmentStats> {
    onOpen {
        setText(24, "Stab +")
    }
}

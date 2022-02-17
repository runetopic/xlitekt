package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceMapping.buildInterfaceListener
import com.runetopic.xlitekt.game.ui.UserInterface

buildInterfaceListener<UserInterface.WornEquipment> {
    onClick("View equipment stats") {
        player.interfaceManager.openInterface(UserInterface.EquipmentStats)
    }

    onClick("View guide prices") {
        player.interfaceManager.openInterface(UserInterface.GuidePrices)
    }
}


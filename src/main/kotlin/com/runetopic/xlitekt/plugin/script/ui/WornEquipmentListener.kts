package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.InterfaceMapping.buildInterfaceListener
import com.runetopic.xlitekt.game.ui.UserInterface

buildInterfaceListener<UserInterface.WornEquipment> {
    onClick("View equipment stats") {
        player.interfaceManager.openModal(UserInterface.EquipmentStats)
    }

    onClick("View guide prices") {
        player.interfaceManager.openModal(UserInterface.GuidePrices)
    }

    onClick("View items kept on death") {
        player.interfaceManager.openModal(UserInterface.ItemsKeptOnDeath)
    }

    onClick("Call follower") { player.interfaceManager.message("You do not have a follower.") }
}

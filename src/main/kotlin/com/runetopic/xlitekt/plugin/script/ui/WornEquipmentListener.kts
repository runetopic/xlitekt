package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.actor.player.Equipment.Companion.SLOT_WEAPON
import com.runetopic.xlitekt.game.item.Item
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface

private val equipmentContainerKey = 94
private val equipmentSize = 15

onInterface<UserInterface.WornEquipment> {
    onOpen {
        // TODO this will be handled inside of the players equipment manager.
        val equipment = MutableList<Item?>(equipmentSize) { null }
        equipment[SLOT_WEAPON] = Item(4151, 1)
        setItems(equipmentContainerKey, equipment.toList())
    }
    onClick("View equipment stats") { player.interfaceManager.openModal(UserInterface.EquipmentStats) }
    onClick("View guide prices") { player.interfaceManager.openModal(UserInterface.GuidePrices) }
    onClick("View items kept on death") { player.interfaceManager.openModal(UserInterface.ItemsKeptOnDeath) }
    onClick("Call follower") { player.interfaceManager.message("You do not have a follower.") }
}

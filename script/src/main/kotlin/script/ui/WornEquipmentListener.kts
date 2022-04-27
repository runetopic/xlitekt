package script.ui

import xlitekt.game.actor.player.message
import xlitekt.game.content.container.equipment.Equipment.Companion.SLOT_MAINHAND
import xlitekt.game.content.item.Item
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.WornEquipment
import xlitekt.game.content.ui.onInterface

private val equipmentContainerKey = 94
private val equipmentSize = 15

onInterface<WornEquipment> {
    onOpen {
        // TODO this will be handled inside of the players equipment manager.
        val equipment = MutableList<Item?>(equipmentSize) { null }
        equipment[SLOT_MAINHAND] = Item(4151, 1)
        setItems(equipmentContainerKey, equipment.toList())
    }
    onClick("View equipment stats") {
        interfaces += UserInterface.EquipmentStats
    }
    onClick("View guide prices") {
        interfaces += UserInterface.GuidePrices
    }
    onClick("View items kept on death") {
        interfaces += UserInterface.ItemsKeptOnDeath
    }
    onClick("Call follower") { message("You do not have a follower.") }
}

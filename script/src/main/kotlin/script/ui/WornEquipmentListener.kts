package script.ui

import xlitekt.game.actor.player.message
import xlitekt.game.content.container.equipment.Equipment.Companion.SLOT_MAINHAND
import xlitekt.game.content.item.Item
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.WornEquipment
import xlitekt.game.content.ui.onInterface
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemExamines

private val equipmentContainerKey = 94
private val equipmentSize = 15
private val npcExamines by inject<ItemExamines>()

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
    onClick("Call follower") {
        message { "You do not have a follower." }
    }
    onClick("*") {
        when (it.index) {
            10 -> {
                val offset = it.childId - 15
                val slot = when {
                    offset >= 9 -> offset + 3
                    offset >= 7 -> offset + 2
                    offset >= 6 -> offset + 1
                    else -> offset
                }
                if (slot < 0 || slot > 13) return@onClick

                message { "$slot" }
                // TODO get item from equipment manager and send player the examine message
            }
        }
    }
}

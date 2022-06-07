package script.ui

import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.UserInterface.WornEquipment
import xlitekt.game.content.ui.onInterface
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemExamines

private val itemExamines by inject<ItemExamines>()

onInterface<WornEquipment> {
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
        val slot = mapEquipmentSlotIds(it.childId)
        if (slot < 0 || slot > 13) return@onClick

        val item = equipment[slot] ?: return@onClick

        when (it.index) {
            1 -> {
                // remove item
                if (inventory.isFull()) {
                    message { "You don't have enough free space to do that." }
                    return@onClick
                }

                equipment.removeItem(slot, item) {
                    inventory.addItem(item) {
                        // play removing sound
                    }
                }
                renderAppearance()
            }
            10 -> { // examine item
                message { itemExamines[item.id]?.message ?: "It's a ${item.entry?.name}" }
            }
        }
    }
}

fun mapEquipmentSlotIds(childId: Int): Int {
    val offset = childId - 15
    return when {
        offset >= 9 -> offset + 3
        offset >= 7 -> offset + 2
        offset >= 6 -> offset + 1
        else -> offset
    }
}

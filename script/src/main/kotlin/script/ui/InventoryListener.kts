package script.ui

import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface

onInterface<UserInterface.Inventory> {
    onOpHeld {
        val slot = it.fromSlotId
        val item = inventory[slot] ?: return@onOpHeld

        if (item.id != it.fromItemId) return@onOpHeld

        when (it.index) {
            2 -> {
                // wear
                equipment.equipItem(item, slot) {
                    renderAppearance()
                }
            }
            5 -> {
                // drop
                inventory.removeItem(slot, item) {
                    zone().requestAddObj(FloorItem(item.id, item.amount, location))
                }
            }
        }
    }
}

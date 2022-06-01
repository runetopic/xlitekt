package script.ui

import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.world.World
import xlitekt.shared.inject

private val world by inject<World>()

onInterface<UserInterface.Inventory> {
    onOpHeld {
        val slot = it.fromSlotId
        val item = inventory[slot] ?: return@onOpHeld
        inventory.removeItem(slot, item) { world.zone(location).requestAddObj(FloorItem(item.id, item.amount, location)) }
    }
}

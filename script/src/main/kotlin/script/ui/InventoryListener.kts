package script.ui

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.shared.inject
import xlitekt.shared.resource.EquipmentSlot
import xlitekt.shared.resource.ItemInfoMap
import xlitekt.shared.resource.ItemInfoResource

val itemInfoMap by inject<ItemInfoMap>()

onInterface<UserInterface.Inventory> {
    onOpHeld {
        val slot = it.fromSlotId
        val item = inventory[slot] ?: return@onOpHeld

        if (item.id != it.fromItemId) return@onOpHeld

        val itemInfo = itemInfoMap[item.id] ?: return@onOpHeld

        when (it.index) {
            2 -> {
                // wear
                wearItem(itemInfo, this@InventoryListener, slot, item)
            }
            5 -> {
                // drop
                inventory.removeItem(slot, item) {
                    zone().requestAddObj(FloorItem(item.id, item.amount, location))
                }

                message {
                    "You dropped ${itemInfo.name}"
                }
            }
        }
    }
}

fun Player.wearItem(
    itemInfo: ItemInfoResource,
    inventoryListener: InventoryListener,
    slot: Int,
    item: Item
) {
    equipment.equipItem(item, slot)// TODO pass slots in
    renderAppearance()
}

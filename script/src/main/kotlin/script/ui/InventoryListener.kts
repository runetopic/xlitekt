package script.ui

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.content.container.equipment.Equipment
import xlitekt.game.content.item.FloorItem
import xlitekt.game.content.item.Item
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.world.World
import xlitekt.shared.inject
import xlitekt.shared.resource.EquipmentSlot
import xlitekt.shared.resource.ItemInfoMap
import xlitekt.shared.resource.ItemInfoResource

private val world by inject<World>()

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
                    world.zone(location).requestAddObj(FloorItem(item.id, item.amount, location))
                }
            }
        }
    }
}

fun mapEquipmentSlot(itemInfo: ItemInfoResource): Int? = when (itemInfo.equipment?.equipmentSlot) {
    EquipmentSlot.WEAPON, EquipmentSlot.TWO_HAND -> Equipment.SLOT_MAINHAND
    EquipmentSlot.AMMO -> Equipment.SLOT_AMMO
    EquipmentSlot.BODY -> Equipment.SLOT_TORSO
    EquipmentSlot.CAPE -> Equipment.SLOT_BACK
    EquipmentSlot.FEET -> Equipment.SLOT_FEET
    EquipmentSlot.HANDS -> Equipment.SLOT_HANDS
    EquipmentSlot.HEAD -> Equipment.SLOT_HEAD
    EquipmentSlot.LEGS -> Equipment.SLOT_LEGS
    EquipmentSlot.NECK -> Equipment.SLOT_NECK
    EquipmentSlot.RING -> Equipment.SLOT_RING
    EquipmentSlot.SHIELD -> Equipment.SLOT_OFFHAND
    else -> null
}

fun Player.wearItem(
    itemInfo: ItemInfoResource,
    inventoryListener: InventoryListener,
    slot: Int,
    item: Item
) {
    if (!itemInfo.equipable) {
        message { "You can't wear that!" }
        return
    }

    val equipmentSlot: Int = inventoryListener.mapEquipmentSlot(itemInfo) ?: return

    inventory.removeItem(slot, item) {
        // TODO this shouldn't use a set here. This needs to check for stackable items and increment the amount if so. (E.g: arrows or other ammo)
        equipment.setItem(equipmentSlot, item) { slot ->
            equipment.refreshSlots(listOf(slot))
        }
    }
    renderAppearance()
}

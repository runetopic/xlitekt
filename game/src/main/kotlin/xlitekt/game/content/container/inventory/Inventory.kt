package xlitekt.game.content.container.inventory

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.UpdateContainerPartialPacket

const val INVENTORY_ID = 149
const val PACKED_INVENTORY_ID = INVENTORY_ID shl 16 or 65536
const val INVENTORY_CONTAINER_KEY = 93
const val INVENTORY_CAPACITY = 28

/**
 * This class is used to track the Player's items in their inventory.
 * @author Tyler Telis
 */
class Inventory(
    val player: Player,
) : Container(INVENTORY_CONTAINER_KEY, INVENTORY_CAPACITY) {

    /**
     * Refresh the player's inventory slots upon login to the world.
     */
    fun login() {
        player.write(
            UpdateContainerFullPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                this,
            )
        )
    }

    /**
     * Empties the player's inventory and then pools the packet to the client.
     */
    fun empty() = this.clear {
        player.write(
            UpdateContainerFullPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                this,
            )
        )
        player.message { "Your inventory has been cleared." }
    }

    /**
     * Adds an item to the player's inventory, and if the item is added we invoke a function with the slotId of the newly added item.
     */
    fun addItem(item: Item, function: (Item).(Int) -> Unit) {
        if (addItem(item)) function.invoke(item, slotId(item))
    }

    fun removeItem(slotId: Int, item: Item, function: (Item).(List<Int>) -> Unit) {
        remove(slotId, item) { slots ->
            refreshSlots(slots)
            function.invoke(item, slots)
        }
    }

    /**
     * Add an item to the player's inventory and sends the client a message if the item cannot be added.
     * @param item The item being added to the inventory.
     * @return whether of not the item has been added to the inventory.
     */
    fun addItem(item: Item): Boolean {
        if (isFull() && !item.stackable) {
            player.message { "You don't have enough inventory space." }
            return false
        }

        val added = add(item) { slots ->
            refreshSlots(slots)
        }

        if (!added) {
            player.message { "You don't have enough inventory space." }
            return false
        }

        return true
    }

    /**
     * Refreshes specific slots within the inventory container.
     * This leverages the UpdateContainerPartialPacket
     */
    private fun refreshSlots(slots: List<Int>) {
        player.write(
            UpdateContainerPartialPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                this,
                slots
            )
        )
    }
}

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
    val player: Player
) {
    /**
     * The backing item container used to emit updates to the client.
     */
    private val container = Container(
        INVENTORY_CONTAINER_KEY,
        INVENTORY_CAPACITY,
    )

    /**
     * Refresh the player's inventory slots upon login to the world.
     */
    fun login() {
        player.write(
            UpdateContainerFullPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                container,
            )
        )
    }

    /**
     * Empties the player's inventory and then pools the packet to the client.
     */
    fun empty() = container.empty {
        player.write(
            UpdateContainerFullPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                container,
            )
        )
        player.message { "Your inventory has been cleared." }
    }

    /**
     * Adds an item to the player's inventory, and if the item is added we invoke a function with the slotId of the newly added item.
     */
    fun add(item: Item, function: (Item).(Int) -> Unit) {
        if (add(item)) function.invoke(item, container.slotId(item))
    }

    /**
     * Add an item to the player's inventory and sends the client a message if the item cannot be added.
     * @param item The item being added to the inventory.
     * @return whether of not the item has been added to the inventory.
     */
    fun add(item: Item): Boolean {
        if (container.isFull() && !item.isStackable()) {
            player.message { "You don't have enough inventory space." }
            return false
        }

        val added = container.add(item) { slots ->
            refreshSlots(slots)
        }

        if (!added) {
            player.message { "You don't have enough inventory space." }
            return false
        }

        return true
    }

    /**
     * Grabs the slot id from the container.
     * @param item The item to find the slot id for.
     */
    fun slotId(item: Item) = container.slotId(item)

    /**
     * Grabs the slot id from the container.
     * @param itemId The item id to find the slot id for.
     */
    fun slotId(itemId: Int) = container.slotId(itemId)

    /**
     * Refreshes specific slots within the inventory container.
     * This leverages the UpdateContainerPartialPacket
     */
    private fun refreshSlots(slots: IntRange) {
        player.write(
            UpdateContainerPartialPacket(
                PACKED_INVENTORY_ID,
                INVENTORY_CONTAINER_KEY,
                container,
                slots
            )
        )
    }
}

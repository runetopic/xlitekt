package xlitekt.game.content.inventory

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.UpdateContainerPartialPacket

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
                149 shl 16 or 65536,
                93,
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
                149 shl 16 or 65536,
                93,
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
     * Refreshes specific slots within the inventory container.
     * This leverages the UpdateContainerPartialPacket
     */
    private fun refreshSlots(slots: IntRange) {
        player.write(
            UpdateContainerPartialPacket(
                149 shl 16 or 65536,
                93,
                container,
                slots
            )
        )
    }
}

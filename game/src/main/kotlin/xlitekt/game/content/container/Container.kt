package xlitekt.game.content.container

import xlitekt.game.content.item.Item

/**
 * This class represents all the items stored on a specific RS container.
 * @author Tyler Telis
 * @param id The id of the container. (E.g: Container key ids in the cache)
 * @param capacity The capacity of the container.
 * @param alwaysStack If this container is always stacking items within the container. (This currently has limited support. Will be adding more in the future)
 */
abstract class Container(
    val id: Int,
    val capacity: Int,
    private val items: MutableList<Item?> = MutableList(capacity) { null },
    val alwaysStack: Boolean = false
) : List<Item?> by items {
    /**
     * Counts the number of free slots in the container.
     */
    fun availableSlots(): Int = items.count { it == null }

    /**
     * Adds an item to the container and invokes a function with the item.
     * If the item's amount exceeds Int.MAX_VALUE, we go ahead and do nothing because of an overflow exception.
     */
    protected fun add(item: Item, slotId: Int = slotId(item), function: (Item).(IntRange) -> Unit): Boolean {
        when {
            item.isStackable() && slotId == -1 -> {
                val nextSlot = nextAvailableSlot()
                if (nextSlot == -1) return false
                items[nextSlot] = item
                function.invoke(item, 0..nextSlot)
                return true
            }
            slotId != -1 -> return replaceItem(slotId, item, function)
            else -> return addItemNonStacking(item, function)
        }
    }

    /**
     * Add an item to the container and invokes the function.
     * This function is only ever used by items that are not stackable.
     * @param item The item to be added to the container.
     * @param function This function is invoked with the item and slots that were updated.
     */
    private fun addItemNonStacking(
        item: Item,
        function: Item.(IntRange) -> Unit
    ): Boolean {
        if (item.isStackable()) return false

        var lastSlot = -1

        repeat(item.amount) {
            val nextSlot = nextAvailableSlot()

            if (nextSlot == -1) {
                function.invoke(item, 0..lastSlot)
                return false
            }

            lastSlot = nextSlot
            items[nextSlot] = item.copy(id = item.id, amount = 1)
        }

        function.invoke(item, 0..lastSlot)
        return true
    }

    /**
     * Deletes an item at a specified slot.
     * If the slot is not specified it will automatically figure out the slot id based on the items in the container.
     * @param item The item to delete
     * @param slotId The slot id of the item in the container.
     * @param function This is the function we invoke with the item deleted and slot effected.
     */
    protected fun remove(item: Item, slotId: Int = slotId(item), function: (Item).(Int) -> Unit): Boolean {
        if (slotId == -1) return false

        items[slotId] = null
        function.invoke(item, slotId)
        return true
    }

    /**
     * Replaces every item with null to clear out the container.
     */
    protected fun clear() = items.replaceAll { null }

    /**
     * Clears the container and invokes a function
     */
    protected fun clear(function: () -> Unit) {
        clear()
        function.invoke()
    }

    /**
     * This returns if the container is full or not.
     */
    fun isFull(): Boolean = availableSlots() == 0

    /**
     * Checks if the container is not full, and we have an available slot to fill.
     */
    fun hasSpace(): Boolean = !isFull() && nextAvailableSlot() != -1

    /**
     * Get the next available slot.
     * This will find the first index of the first null item it encounters.
     */
    fun nextAvailableSlot(): Int = items.indexOfFirst { it == null }

    /**
     * Replaces an item at a slotId
     * If the existing item is found, and it's stackable we just increment the existing item's amount.
     * @param slotId The slot id that we are replacing the item at.
     * @param item The item we're replacing at the slot.
     * @param function This function is invoked with the item and slots that were updated.
     */
    private fun replaceItem(
        slotId: Int,
        item: Item,
        function: Item.(IntRange) -> Unit
    ): Boolean {
        val existingItem = items[slotId] ?: return false

        if ((item.amount + existingItem.amount) < 0) return false

        return when {
            item.isStackable() && !isFull() -> {
                items[slotId] = item.copy(
                    id = item.id,
                    amount = item.amount + existingItem.amount
                )
                function.invoke(item, 0..slotId)
                true
            }
            else -> addItemNonStacking(item, function)
        }
    }

    /**
     * Sets a specific item at a desired slot id.
     * @param slotId The slot id we're placing this item into.
     * @param item The item to set
     * @param function This function is invoked with the item and slots that were updated.
     */
    fun setItem(slotId: Int, item: Item, function: (Item).(Int) -> Unit) {
        items[slotId] = item
        function.invoke(item, slotId)
    }

    /**
     * Gets the slotId for the provided item id.
     * @param id The item id to look up.
     */
    fun slotId(id: Int) = items.indexOfFirst { it?.id == id }

    /**
     * Gets the slotId for the provided item.
     * @param item The item to lookup
     */
    fun slotId(item: Item) = items.indexOfFirst { it?.id == item.id }

    /**
     * Finds the first item by the slot id.
     * @param slot The slot id to search for.
     * @return The item found within the container.
     */
    fun firstBySlot(slot: Int): Item? = items[slot]
}

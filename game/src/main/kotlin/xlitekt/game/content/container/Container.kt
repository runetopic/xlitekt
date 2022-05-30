package xlitekt.game.content.container

import xlitekt.game.content.item.Item

data class Container(
    val id: Int,
    val capacity: Int,
    private val items: MutableList<Item?> = MutableList(capacity) { null },
    val alwaysStack: Boolean = false
) : List<Item?> by items {
    /**
     * Replaces every item with null to clear out the container.
     */
    private fun empty() = items.replaceAll { null }

    /**
     * Empties the container and invokes a function
     */
    fun empty(function: () -> Unit) {
        empty()
        function.invoke()
    }

    fun setItem(slotId: Int, item: Item, function: (Item).(Int) -> Unit) {
        items[slotId] = item
        function.invoke(item, slotId)
    }

    /**
     * Adds an item to the container and invokes a function with the item.
     * If an existing item is found, and it's stackable we just increment the existing item's amount.
     * If the item's amount exceeds Int.MAX_VALUE, we go ahead and do nothing because of an overflow exception.
     */
    fun add(item: Item, slotId: Int = slotId(item), function: (Item).(IntRange) -> Unit): Boolean {
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
     * Replaces an item at a slotId
     * @param slotId The slot id that we are replacing the item at.
     * @param item The item we're replace at the slot.
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
     * Add an item to the container and invokes the function.
     * This function is only ever used by items that are not stackable.
     * @param item The item to be added to the container.
     * @param function This function is invoked with the item and slots that were updated.
     */
    private fun addItemNonStacking(
        item: Item,
        function: Item.(IntRange) -> Unit
    ): Boolean {
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
     * Gets the slotId for the provided item id.
     * @param id The item id to look up.
     */
    fun slotId(id: Int) = items.indexOfFirst { it?.id == id }

    /**
     * Gets the slotId for the provided item id.
     * @param item The item to lookup
     */
    fun slotId(item: Item) = items.indexOfFirst { it?.id == item.id }

    /**
     * Counts the number of free slots in the container.
     */
    private fun availableSlots(): Int = items.count { it == null }

    /**
     * Get the next available slot.
     * This will find the first index of the first null item it encounters.
     */
    fun nextAvailableSlot(): Int = items.indexOfFirst { it == null }

    /**
     * This returns if the container is full or not.
     */
    fun isFull(): Boolean = availableSlots() == 0
}

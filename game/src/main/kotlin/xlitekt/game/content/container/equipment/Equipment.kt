package xlitekt.game.content.container.equipment

import xlitekt.game.actor.player.Player
import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.UpdateContainerPartialPacket

const val EQUIPMENT_KEY = 94

class Equipment(
    val player: Player
) {
    /**
     * The backing container for the equipment interfaces.
     */
    val container = Container(
        -1,
        15,
    )

    /**
     * Sends the initial player's equipment on login.
     */
    fun login() {
        container.setItem(SLOT_MAINHAND, Item(4151, 1)) {
            player.write(
                UpdateContainerFullPacket(
                    -1,
                    EQUIPMENT_KEY,
                    container
                )
            )
        }
    }

    /**
     * Grabs the slot id from the container.
     * @param item The item to find the slot id for.
     */
    fun slotId(item: Item) = container.slotId(item)

    /**
     * Grabs the slot id from the container.
     * @param itemId The item id to find the slot id for.
     * @return The slot id found.
     */
    fun slotId(itemId: Int) = container.slotId(itemId)

    /**
     * Finds the first item by the slotId.
     * @param slotId The slot id to search for.
     * @return Nullable Item.
     */
    fun findFirstBySlot(slotId: Int) = container[slotId]

    /**
     * Refreshes specific slots within the equipment container.
     * This leverages the UpdateContainerPartialPacket
     */
    private fun refreshSlots(slots: IntRange) {
        player.write(
            UpdateContainerPartialPacket(
                -1,
                EQUIPMENT_KEY,
                container,
                slots
            )
        )
    }

    companion object {
        const val SLOT_HEAD = 0
        const val SLOT_BACK = 1
        const val SLOT_NECK = 2
        const val SLOT_MAINHAND = 3
        const val SLOT_TORSO = 4
        const val SLOT_OFFHAND = 5
        const val SLOT_LEG = 7
        const val SLOT_HANDS = 9
        const val SLOT_FEET = 10
        const val SLOT_RING = 12
        const val SLOT_AMMO = 13
    }
}

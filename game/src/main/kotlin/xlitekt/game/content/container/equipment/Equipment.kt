package xlitekt.game.content.container.equipment

import kotlinx.serialization.Serializable
import xlitekt.game.actor.player.Player
import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.UpdateContainerPartialPacket
import xlitekt.game.packet.UpdateWeightPacket
import xlitekt.shared.inject
import xlitekt.shared.resource.ItemInfoMap

const val EQUIPMENT_KEY = 94
const val EQUIPMENT_CAPACITY = 15

@Serializable
class Equipment(
    val player: Player
) : Container(-1, EQUIPMENT_CAPACITY) {

    val ammo: Item? get() = this[SLOT_AMMO]
    val back: Item? get() = this[SLOT_BACK]
    val head: Item? get() = this[SLOT_HEAD]
    val neck: Item? get() = this[SLOT_NECK]
    val torso: Item? get() = this[SLOT_TORSO]
    val legs: Item? get() = this[SLOT_LEGS]
    val mainhand: Item? get() = this[SLOT_MAINHAND]
    val offhand: Item? get() = this[SLOT_OFFHAND]
    val hands: Item? get() = this[SLOT_HANDS]
    val feet: Item? get() = this[SLOT_FEET]
    val ring: Item? get() = this[SLOT_RING]

    /**
     * Sends the initial player's equipment on login.
     */
    fun login() {
        player.write(
            UpdateContainerFullPacket(
                -1,
                EQUIPMENT_KEY,
                this
            )
        )

        updateWeight(true)
    }

    /**
     * Removes an item from the equipment container.
     * @param slotId The slot id that we want to remove it from.
     * @param item The item we're removing from the container.
     * @param amount The amount that we want to remove from the container.
     * @param function This is the function we invoke if we successfully removed an item.
     */
    fun removeItem(slotId: Int, item: Item, amount: Int = item.amount, function: (Item).(Int) -> Unit) {
        remove(slotId, item, amount) { slots ->
            refreshSlots(slots)
            function.invoke(item, slotId)
        }
    }

    /**
     * Calculates the players weight based on the worn equipment items and writes it to the client if toggled.
     */
    fun updateWeight(write: Boolean = false) {
        filterNotNull().forEach {
            val info = itemInfoMap[it.id] ?: return

            player.weight += info.weight
        }

        if (write) player.write(
            UpdateWeightPacket(player.weight)
        )
    }

    /**
     * Refreshes specific slots within the equipment container and calculate the newly refreshed bonuses.
     * This leverages the UpdateContainerPartialPacket
     */
    fun refreshSlots(slots: List<Int>) {
        player
            .bonuses
            .calculateEquippedBonuses(player.equipment) // TODO: potentially only calculate the refreshed slots.

        player.write(
            UpdateContainerPartialPacket(
                -1,
                EQUIPMENT_KEY,
                this,
                slots
            )
        )

        updateWeight(true)
    }

    companion object {
        const val SLOT_HEAD = 0
        const val SLOT_BACK = 1
        const val SLOT_NECK = 2
        const val SLOT_MAINHAND = 3
        const val SLOT_TORSO = 4
        const val SLOT_OFFHAND = 5
        const val SLOT_LEGS = 7
        const val SLOT_HANDS = 9
        const val SLOT_FEET = 10
        const val SLOT_RING = 12
        const val SLOT_AMMO = 13

        private val itemInfoMap by inject<ItemInfoMap>()
    }
}

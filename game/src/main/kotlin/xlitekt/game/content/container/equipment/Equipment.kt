package xlitekt.game.content.container.equipment

import xlitekt.game.actor.player.Player
import xlitekt.game.content.container.Container
import xlitekt.game.content.item.Item
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.UpdateContainerPartialPacket

const val EQUIPMENT_KEY = 94
const val EQUIPMENT_CAPACITY = 15

class Equipment(
    val player: Player
) : Container(-1, EQUIPMENT_CAPACITY) {

    val ammo: Item? get() = this[SLOT_AMMO]
    val back: Item? get() = this[SLOT_BACK]
    val head: Item? get() = this[SLOT_HEAD]
    val neck: Item? get() = this[SLOT_NECK]
    val torso: Item? get() = this[SLOT_TORSO]
    val legs: Item? get() = this[SLOT_LEG]
    val weapon: Item? get() = this[SLOT_MAINHAND]
    val offhand: Item? get() = this[SLOT_OFFHAND]
    val hands: Item? get() = this[SLOT_HANDS]
    val feet: Item? get() = this[SLOT_FEET]
    val ring: Item? get() = this[SLOT_RING]

    /**
     * Sends the initial player's equipment on login.
     */
    fun login() {
        setItem(SLOT_MAINHAND, Item(4151, 1)) {
            player.write(
                UpdateContainerFullPacket(
                    -1,
                    EQUIPMENT_KEY,
                    this@Equipment
                )
            )
        }
    }

    /**
     * Refreshes specific slots within the equipment container.
     * This leverages the UpdateContainerPartialPacket
     */
    private fun refreshSlots(slots: List<Int>) {
        player.write(
            UpdateContainerPartialPacket(
                -1,
                EQUIPMENT_KEY,
                this,
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

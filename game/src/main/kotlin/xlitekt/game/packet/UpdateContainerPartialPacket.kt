package xlitekt.game.packet

import xlitekt.game.item.Item

/**
 * @author Tyler Telis
 */
data class UpdateContainerPartialPacket(
    val packedInterface: Int,
    val containerKey: Int,
    val items: List<Item?>,
    val slots: List<Int>
) : Packet

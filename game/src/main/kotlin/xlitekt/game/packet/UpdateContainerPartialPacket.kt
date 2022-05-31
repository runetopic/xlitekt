package xlitekt.game.packet

import xlitekt.game.content.item.Item

/**
 * @author Tyler Telis
 */
data class UpdateContainerPartialPacket(
    val packedInterface: Int,
    val containerKey: Int,
    val items: List<Item?>,
    val slots: IntRange
) : Packet

package xlitekt.game.packet

import xlitekt.game.content.item.Item

/**
 * @author Tyler Telis
 */
data class UpdateContainerFullPacket(
    val packedInterface: Int,
    val containerKey: Int,
    val items: List<Item?>
) : Packet

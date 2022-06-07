package xlitekt.game.packet

import xlitekt.game.content.ui.InterfaceEvent

/**
 * @author Tyler Telis
 */
data class IfSetEventsPacket(
    val packedInterface: Int,
    val fromSlot: Int,
    val toSlot: Int,
    val event: Int
) : Packet

package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * Represents an IF_BUTTON client -> server packet.
 * @property index The index of this packet followed by IF_BUTTON. Examples: IF_BUTTON1, IF_BUTTON2.
 * @property packedInterface The packed interface that was clicked by the client. (interfaceId shl 16 or childId).
 * @property slotId The slotId clicked by the client. This defaults to 65535 if there was no container slot clicked.
 * @property itemId The itemId clicked by the client. This defaults to 65535 if there was no item clicked.
 */
data class IfButtonPacket(
    val index: Int,
    val packedInterface: Int,
    val slotId: Int,
    val itemId: Int
) : Packet

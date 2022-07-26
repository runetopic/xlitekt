package xlitekt.game.packet

/**
 * <b>Information</b>
 *
 * Represents the IF_BUTTON client -> server packet.
 *
 * This packet is used to handle when the client clicks on an interface.
 *
 * <b>Disassembly Example</b>
 *
 * ```
 * val index = 1,
 * val packedInterface = readInt(),
 * val slotId = readUShort(),
 * val itemId = readUShort()
 * ```
 *
 * @author Jordan Abraham
 *
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

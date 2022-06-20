package xlitekt.game.packet

/**
 * @author Jordan Abraham
 *
 * Represents the OBJ_ADD server -> client packet.
 *
 * This packet is used to add an obj to a zone.
 *
 * @property id The id of the obj.
 * @property amount The amount of the obj.
 * @property packedOffset The packed offset location of the obj relative to the client player location.
 */
data class ObjAddPacket(
    val id: Int,
    val amount: Int,
    val packedOffset: Int
) : Packet

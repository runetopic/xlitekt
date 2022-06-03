package xlitekt.game.packet

data class OpLocPacket(
    val index: Int,
    val objectId: Int,
    val x: Int,
    val z: Int,
    val isModified: Boolean
) : Packet

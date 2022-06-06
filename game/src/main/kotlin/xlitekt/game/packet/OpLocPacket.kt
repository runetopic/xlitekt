package xlitekt.game.packet

data class OpLocPacket(
    val index: Int,
    val locId: Int,
    val x: Int,
    val z: Int,
    val running: Boolean
) : Packet

package xlitekt.game.packet

data class OpNPCPacket(
    val npcIndex: Int,
    val index: Int,
    val isModifiedClick: Boolean
) : Packet

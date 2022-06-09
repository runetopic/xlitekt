package xlitekt.game.packet.assembler

import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PacketAssembler(
    val opcode: Int,
    val size: Int,
    val packet: Packet.() -> ByteArray
)

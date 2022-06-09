package xlitekt.game.packet.disassembler

import xlitekt.game.packet.Packet
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
data class PacketDisassembler(
    val size: Int,
    val packet: ByteBuffer.() -> Packet
)

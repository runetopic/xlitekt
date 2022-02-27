package xlitekt.game.packet.disassembler

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PacketDisassembler(
    val size: Int,
    val packet: ByteReadPacket.() -> Packet
)

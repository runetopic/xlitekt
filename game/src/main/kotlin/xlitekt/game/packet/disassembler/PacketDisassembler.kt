package xlitekt.game.packet.disassembler

import io.ktor.utils.io.ByteReadChannel
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
data class PacketDisassembler(
    val size: Int,
    val packet: suspend ByteReadChannel.(Int) -> Packet
)

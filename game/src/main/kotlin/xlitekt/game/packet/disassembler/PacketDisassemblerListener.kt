package xlitekt.game.packet.disassembler

import io.ktor.utils.io.ByteReadChannel
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
object PacketDisassemblerListener {
    val listeners = mutableMapOf<Int, PacketDisassembler>()
}

inline fun <reified T : Packet> onPacketDisassembler(opcode: Int, size: Int, noinline packet: suspend ByteReadChannel.(Int) -> T) {
    PacketDisassemblerListener.listeners[opcode] = PacketDisassembler(size, packet)
}

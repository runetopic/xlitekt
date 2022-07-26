package xlitekt.game.packet.disassembler

import io.ktor.utils.io.ByteReadChannel
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
class PacketDisassemblerListener : MutableMap<Int, PacketDisassembler> by mutableMapOf() {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Packet> disassemblePacket(opcode: Int, size: Int, noinline packet: suspend ByteReadChannel.(Int) -> T) {
        this[opcode] = PacketDisassembler(size, packet)
    }
}

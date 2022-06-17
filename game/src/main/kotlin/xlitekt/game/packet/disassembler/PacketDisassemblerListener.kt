package xlitekt.game.packet.disassembler

import io.ktor.utils.io.ByteReadChannel
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
class PacketDisassemblerListener(
    val disassemblers: MutableMap<Int, PacketDisassembler> = mutableMapOf()
) : Map<Int, PacketDisassembler> by disassemblers {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Packet> disassemblePacket(opcode: Int, size: Int, noinline packet: suspend ByteReadChannel.(Int) -> T) {
        disassemblers[opcode] = PacketDisassembler(size, packet)
    }
}

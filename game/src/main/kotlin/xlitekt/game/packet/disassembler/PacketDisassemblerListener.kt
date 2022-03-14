package xlitekt.game.packet.disassembler

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
object PacketDisassemblerListener {
    val listeners = mutableMapOf<Int, PacketDisassembler>()
}

inline fun <reified T : Packet> onPacketDisassembler(opcodes: IntArray, size: Int, noinline packet: ByteReadPacket.() -> T) {
    opcodes.forEach { opcode ->
        onPacketDisassembler(opcode, size, packet)
    }
}

inline fun <reified T : Packet> onPacketDisassembler(opcode: Int, size: Int, noinline packet: ByteReadPacket.() -> T) {
    PacketDisassemblerListener.listeners[opcode] = PacketDisassembler(size, packet)
}

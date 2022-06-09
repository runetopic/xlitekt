package xlitekt.game.packet.disassembler

import xlitekt.game.packet.Packet
import java.nio.ByteBuffer

/**
 * @author Jordan Abraham
 */
object PacketDisassemblerListener {
    val listeners = mutableMapOf<Int, PacketDisassembler>()
}

inline fun <reified T : Packet> onPacketDisassembler(opcodes: IntArray, size: Int, noinline packet: ByteBuffer.() -> T) {
    opcodes.forEach { opcode ->
        onPacketDisassembler(opcode, size, packet)
    }
}

inline fun <reified T : Packet> onPacketDisassembler(opcode: Int, size: Int, noinline packet: ByteBuffer.() -> T) {
    PacketDisassemblerListener.listeners[opcode] = PacketDisassembler(size, packet)
}

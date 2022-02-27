package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
object PacketDisassemblerListener {
    val listeners = mutableMapOf<Int, PacketDisassembler>()
}

inline fun <reified T : Packet> onPacketDisassembler(opcode: Int, size: Int, noinline packet: ByteReadPacket.() -> T) {
    PacketDisassemblerListener.listeners[opcode] = PacketDisassembler(opcode, size, packet)
}

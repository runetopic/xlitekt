package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.Packet
import io.ktor.utils.io.core.ByteReadPacket
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PacketDisassemblerListener {
    val listeners = mutableMapOf<KClass<*>, PacketDisassembler>()
}

inline fun <reified T : Packet> onPacketDisassembler(opcode: Int, size: Int, noinline packet: ByteReadPacket.() -> T) {
    PacketDisassemblerListener.listeners[T::class] = PacketDisassembler(opcode, size, packet)
}

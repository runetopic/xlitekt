package com.runetopic.xlitekt.network.packet.disassembler.handler

import com.runetopic.xlitekt.network.packet.Packet
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PacketHandlerMapping {
    val map = mutableMapOf<KClass<*>, PacketHandlerEvent<Packet>.() -> Unit>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Packet> onPacket(noinline listener: PacketHandlerEvent<T>.() -> Unit) {
    PacketHandlerMapping.map[T::class] = listener as PacketHandlerEvent<Packet>.() -> Unit
}

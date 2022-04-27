package xlitekt.game.packet.disassembler.handler

import kotlin.reflect.KClass
import xlitekt.game.packet.Packet

/**
 * @author Jordan Abraham
 */
object PacketHandlerListener {
    val listeners = mutableMapOf<KClass<*>, PacketHandler<Packet>.() -> Unit>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Packet> onPacketHandler(noinline listener: PacketHandler<T>.() -> Unit) {
    PacketHandlerListener.listeners[T::class] = listener as PacketHandler<Packet>.() -> Unit
}

package xlitekt.game.packet.disassembler.handler

import xlitekt.game.packet.Packet
import kotlin.reflect.KClass

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

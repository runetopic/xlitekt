package xlitekt.game.packet.disassembler.handler

import xlitekt.game.packet.Packet
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class PacketHandlerListener(
    val handlers: MutableMap<KClass<*>, PacketHandler<Packet>.() -> Unit> = mutableMapOf()
) : Map<KClass<*>, PacketHandler<Packet>.() -> Unit> by handlers {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Packet> handlePacket(noinline listener: PacketHandler<T>.() -> Unit) {
        handlers[T::class] = listener as PacketHandler<Packet>.() -> Unit
    }
}

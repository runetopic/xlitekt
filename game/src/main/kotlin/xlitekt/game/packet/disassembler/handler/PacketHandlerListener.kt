package xlitekt.game.packet.disassembler.handler

import xlitekt.game.packet.Packet
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class PacketHandlerListener : MutableMap<KClass<*>, PacketHandler<Packet>.() -> Unit> by mutableMapOf() {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Packet> handlePacket(noinline listener: PacketHandler<T>.() -> Unit) {
        this[T::class] = listener as PacketHandler<Packet>.() -> Unit
    }
}

package xlitekt.game.packet.assembler

import io.ktor.utils.io.core.ByteReadPacket
import xlitekt.game.packet.Packet
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
object PacketAssemblerListener {
    val listeners = mutableMapOf<KClass<*>, PacketAssembler>()
}

@Suppress("UNCHECKED_CAST")
inline fun <reified T : Packet> onPacketAssembler(opcode: Int, size: Int, noinline packet: T.() -> ByteReadPacket) {
    PacketAssemblerListener.listeners[T::class] = PacketAssembler(opcode, size, packet as Packet.() -> ByteReadPacket)
}

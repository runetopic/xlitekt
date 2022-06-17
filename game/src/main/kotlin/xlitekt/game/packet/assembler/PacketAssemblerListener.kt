package xlitekt.game.packet.assembler

import xlitekt.game.packet.Packet
import java.nio.ByteBuffer
import kotlin.reflect.KClass

/**
 * @author Jordan Abraham
 */
class PacketAssemblerListener(
    val assemblers: MutableMap<KClass<*>, PacketAssembler> = mutableMapOf()
) : Map<KClass<*>, PacketAssembler> by assemblers {
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : Packet> assemblePacket(opcode: Int, size: Int, noinline packet: T.(ByteBuffer) -> Unit) {
        assemblers[T::class] = PacketAssembler(opcode, size, packet as Packet.(ByteBuffer) -> Unit)
    }
}

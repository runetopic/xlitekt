package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.UpdateContainerPartialPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeSmart
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateContainerPartialPacket>(opcode = 84, size = -2) {
    buildPacket {
        writeInt(packedInterface)
        writeShort(containerKey.toShort())
        slots.forEach {
            if (it >= items.size) {
                return@forEach
            }
            writeSmart(it)
            val item = items[it]
            val id = item?.id ?: -1
            val amount = item?.amount ?: 0
            if (id != -1) {
                writeShort((id + 1).toShort())
                writeByte(min(amount, 0xff).toByte())
                if (amount >= 0xff) {
                    writeInt(amount)
                }
            }
        }
    }
}

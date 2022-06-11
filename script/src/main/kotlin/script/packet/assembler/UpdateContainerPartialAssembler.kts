package script.packet.assembler

import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.UpdateContainerPartialPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildDynamicPacket
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeSmart
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateContainerPartialPacket>(opcode = 84, size = -2) {
    buildDynamicPacket {
        writeInt(packedInterface)
        writeShort(containerKey)
        for (slot in slots) {
            if (slot >= items.size) {
                break
            }
            writeSmart(slot)
            val item = items[slot]
            val id = item?.id ?: -1
            val amount = item?.amount ?: 0
            writeShort(id + 1)
            if (id != -1) {
                writeByte(min(amount, 0xff))
                if (amount >= 0xff) {
                    writeInt(amount)
                }
            }
        }
    }
}

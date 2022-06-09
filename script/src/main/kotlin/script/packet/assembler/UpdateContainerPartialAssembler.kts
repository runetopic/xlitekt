package script.packet.assembler

import xlitekt.game.packet.UpdateContainerPartialPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeSmart
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateContainerPartialPacket>(opcode = 84, size = -2) {
    allocateDynamic(256) {
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

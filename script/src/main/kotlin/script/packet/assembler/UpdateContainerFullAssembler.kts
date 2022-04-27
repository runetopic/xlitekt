package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortLittleEndian
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateContainerFullPacket>(opcode = 88, size = -2) {
    buildPacket {
        writeInt { packedInterface }
        writeShort { containerKey }
        writeShort(items::size)
        repeat(items.size) {
            val item = items[it]
            val id = item?.id ?: -1
            val amount = item?.amount ?: 0
            writeByteAdd { min(amount, 0xff) }
            if (amount >= 0xff) {
                writeInt { amount }
            }
            writeShortLittleEndian { id + 1 }
        }
    }
}

package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.packet.UpdateContainerFullPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByteAdd
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdateContainerFullPacket>(opcode = 88, size = -2) {
    buildPacket {
        writeInt(packedInterface)
        writeShort(containerKey.toShort())
        items.let { items ->
            writeShort(items.size.toShort())
            repeat(items.size) {
                val item = items[it]
                val id = item?.id ?: -1
                val amount = item?.amount ?: 0
                writeByteAdd(min(amount, 0xff).toByte())
                if (amount >= 0xff) {
                    writeInt(amount)
                }
                writeShortLittleEndian((id + 1).toShort())
            }
        }
    }
}

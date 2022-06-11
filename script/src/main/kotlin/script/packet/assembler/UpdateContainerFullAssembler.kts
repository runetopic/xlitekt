package script.packet.assembler

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
    it.writeInt(packedInterface)
    it.writeShort(containerKey)
    it.writeShort(items.size)
    repeat(items.size) { slot ->
        val item = items[slot]
        val id = item?.id ?: -1
        val amount = item?.amount ?: 0
        it.writeByteAdd(min(amount, 0xff))
        if (amount >= 0xff) {
            it.writeInt(amount)
        }
        it.writeShortLittleEndian(id + 1)
    }
}

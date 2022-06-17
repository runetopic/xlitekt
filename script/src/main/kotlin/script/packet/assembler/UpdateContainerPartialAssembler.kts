package script.packet.assembler

import xlitekt.game.packet.UpdateContainerPartialPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeSmart
import xlitekt.shared.insert
import kotlin.math.min

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateContainerPartialPacket>(opcode = 84, size = -2) {
    it.writeInt(packedInterface)
    it.writeShort(containerKey)
    for (slot in slots) {
        if (slot >= items.size) {
            break
        }
        it.writeSmart(slot)
        val item = items[slot]
        val id = item?.id ?: -1
        val amount = item?.amount ?: 0
        it.writeShort(id + 1)
        if (id != -1) {
            it.writeByte(min(amount, 0xff))
            if (amount >= 0xff) {
                it.writeInt(amount)
            }
        }
    }
}

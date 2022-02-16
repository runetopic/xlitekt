package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateContainerFullPacket
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import kotlin.math.min

/**
 * @author Tyler Telis
 */
class UpdateContainerFullPacketAssembler : PacketAssembler<UpdateContainerFullPacket>(opcode = 88, size = -2) {
    override fun assemblePacket(packet: UpdateContainerFullPacket) = buildPacket {
        writeInt(packet.packedInterface)
        writeShort(packet.containerKey.toShort())
        packet.items.let { items ->
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

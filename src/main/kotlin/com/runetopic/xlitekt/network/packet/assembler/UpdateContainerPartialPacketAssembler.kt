package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateContainerPartialPacket
import com.runetopic.xlitekt.util.ext.writeSmart
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import kotlin.math.min

class UpdateContainerPartialPacketAssembler : PacketAssembler<UpdateContainerPartialPacket>(opcode = 84, size = -2) {
    override fun assemblePacket(packet: UpdateContainerPartialPacket) = buildPacket {
        writeInt(packet.packedInterface)
        writeShort(packet.containerKey.toShort())
        packet.slots.let { slots ->
            slots.forEach {
                if (it >= packet.items.size) {
                    return@forEach
                }

                writeSmart(it)

                val item = packet.items[it]
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
}

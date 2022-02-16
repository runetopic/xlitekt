package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.shared.buffer.writeIntV1
import com.runetopic.xlitekt.shared.buffer.writeShortLittleEndianAdd
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class VarpLargePacketAssembler : PacketAssembler<VarpLargePacket>(opcode = 0, size = 6) {
    override fun assemblePacket(packet: VarpLargePacket) = buildPacket {
        writeIntV1(packet.value)
        writeShortLittleEndianAdd(packet.id.toShort())
    }
}

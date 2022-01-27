package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.util.ext.writeIntV1
import com.runetopic.xlitekt.util.ext.writeShortAddLittle

class VarpLargePacketAssembler : PacketAssembler<VarpLargePacket>(opcode = 0, size = 6) {
    override fun assemblePacket(packet: VarpLargePacket) = buildPacket {
        writeIntV1(packet.value)
        writeShortAddLittle(packet.id.toShort())
    }
}

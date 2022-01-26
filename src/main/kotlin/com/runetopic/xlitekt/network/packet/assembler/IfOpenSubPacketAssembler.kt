package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.util.ext.toInt
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian

class IfOpenSubPacketAssembler : PacketAssembler<IfOpenSubPacket>(opcode = 2, size = 7) {
    override fun assemblePacket(packet: IfOpenSubPacket) = buildPacket {
        writeShortLittleEndian(packet.interfaceId.toShort())
        writeByteSubtract(packet.isWalkable.toInt().toByte())
        writeInt(packet.hash)
    }
}

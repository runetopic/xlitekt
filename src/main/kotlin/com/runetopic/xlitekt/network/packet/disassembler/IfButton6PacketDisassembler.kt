package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class IfButton6PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 64, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket) = IfButtonPacket(
        index = 6,
        packedInterface = packet.readInt(),
        slotId = packet.readUShort().toInt(),
        itemId = packet.readUShort().toInt()
    )
}

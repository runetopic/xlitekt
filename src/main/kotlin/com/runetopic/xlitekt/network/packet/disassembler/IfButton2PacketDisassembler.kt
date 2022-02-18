package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class IfButton2PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 11, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket) = IfButtonPacket(
        index = 2,
        packedInterface = packet.readInt(),
        slotId = packet.readUShort().toInt(),
        itemId = packet.readUShort().toInt()
    )
}

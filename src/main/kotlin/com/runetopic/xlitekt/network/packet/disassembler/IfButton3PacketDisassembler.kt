package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class IfButton3PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 29, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket) = IfButtonPacket(
        index = 3,
        packedInterface = packet.readInt(),
        slotId = packet.readUShort().toInt(),
        itemId = packet.readUShort().toInt()
    )
}

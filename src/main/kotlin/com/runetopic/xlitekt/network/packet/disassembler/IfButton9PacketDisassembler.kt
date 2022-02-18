package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class IfButton9PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 10, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket) = IfButtonPacket(
        index = 9,
        packedInterface = packet.readInt(),
        slotId = packet.readUShort().toInt(),
        itemId = packet.readUShort().toInt()
    )
}

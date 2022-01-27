package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

class IfButton5PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 75, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket): IfButtonPacket {
        val packedInterface = packet.readInt()
        val slotId = packet.readUShort().toInt()
        val itemId = packet.readUShort().toInt()
        return IfButtonPacket(5, packedInterface, slotId, itemId)
    }
}

package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class IfButton7PacketDisassembler : PacketDisassembler<IfButtonPacket>(opcode = 20, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket): IfButtonPacket {
        val packedInterface = packet.readInt()
        val slotId = packet.readUShort().toInt()
        val itemId = packet.readUShort().toInt()
        return IfButtonPacket(7, packedInterface, slotId, itemId)
    }
}

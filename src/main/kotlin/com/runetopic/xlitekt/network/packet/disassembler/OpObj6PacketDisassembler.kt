package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpObjPacket
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readShortLittleEndian

/**
 * @author Jordan Abraham
 */
class OpObj6PacketDisassembler : PacketDisassembler<OpObjPacket>(opcode = 100, size = 2) {
    override fun disassemblePacket(packet: ByteReadPacket): OpObjPacket {
        val itemId = packet.readShortLittleEndian().toUShort().toInt()
        return OpObjPacket(6, -1, 0xffff, itemId)
    }
}

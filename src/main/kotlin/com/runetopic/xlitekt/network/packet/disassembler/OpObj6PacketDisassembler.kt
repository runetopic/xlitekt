package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpObjPacket
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndian
import io.ktor.utils.io.core.ByteReadPacket

/**
 * @author Jordan Abraham
 */
class OpObj6PacketDisassembler : PacketDisassembler<OpObjPacket>(opcode = 100, size = 2) {
    override fun disassemblePacket(packet: ByteReadPacket) = OpObjPacket(
        index = 6,
        packedInterface = -1,
        slotId = 0xffff,
        itemId = packet.readUShortLittleEndian()
    )
}

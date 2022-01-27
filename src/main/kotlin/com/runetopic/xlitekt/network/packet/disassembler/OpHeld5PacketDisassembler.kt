package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpHeldPacket
import com.runetopic.xlitekt.util.ext.readIntV2
import com.runetopic.xlitekt.util.ext.readUShortLittleEndianSubtract
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class OpHeld5PacketDisassembler : PacketDisassembler<OpHeldPacket>(opcode = 43, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket): OpHeldPacket {
        val packedInterface = packet.readIntV2()
        val slotId = packet.readUShortLittleEndianSubtract()
        val itemId = packet.readUShort().toInt()
        return OpHeldPacket(5, packedInterface, slotId, itemId, -1, 0xffff, 0xffff)
    }
}

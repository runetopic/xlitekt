package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpHeldPacket
import com.runetopic.xlitekt.shared.buffer.readIntV2
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndianSubtract
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
class OpHeld5PacketDisassembler : PacketDisassembler<OpHeldPacket>(opcode = 43, size = 8) {
    override fun disassemblePacket(packet: ByteReadPacket) = OpHeldPacket(
        index = 5,
        fromPackedInterface = packet.readIntV2(),
        fromSlotId = packet.readUShortLittleEndianSubtract(),
        fromItemId = packet.readUShort().toInt(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}

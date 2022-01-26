package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateStatPacket
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeIntV1

class UpdateStatAssembler : PacketAssembler<UpdateStatPacket>(opcode = 34, size = 6) {
    override fun assemblePacket(packet: UpdateStatPacket) = buildPacket {
        writeByte(packet.level.toByte())
        writeByteSubtract(packet.skillId.toByte())
        writeIntV1(packet.xp.toInt())
    }
}

package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateStatPacket
import com.runetopic.xlitekt.util.ext.writeByteSubtract
import com.runetopic.xlitekt.util.ext.writeIntV1

class UpdateStatAssembler : PacketAssembler<UpdateStatPacket>(opcode = 34, size = 6) {
    override fun assemblePacket(message: UpdateStatPacket) = buildPacket {
        writeByte(message.level.toByte())
        writeByteSubtract(message.skillId.toByte())
        writeIntV1(message.xp.toInt())
    }
}

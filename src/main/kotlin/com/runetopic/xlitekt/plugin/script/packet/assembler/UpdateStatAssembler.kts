package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateStatPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeByteSubtract
import com.runetopic.xlitekt.shared.buffer.writeIntV1
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateStatPacket>(opcode = 34, size = 6) {
    buildPacket {
        writeByte(level.toByte())
        writeByteSubtract(skillId.toByte())
        writeIntV1(xp.toInt())
    }
}

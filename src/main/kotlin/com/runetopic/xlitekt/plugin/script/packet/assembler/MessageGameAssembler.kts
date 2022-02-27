package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeSmart
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<MessageGamePacket>(opcode = 69, size = -1) {
    buildPacket {
        writeSmart(type)
        writeByte(hasPrefix.toByte())
        if (hasPrefix) writeStringCp1252NullTerminated(prefix)
        writeStringCp1252NullTerminated(message)
    }
}

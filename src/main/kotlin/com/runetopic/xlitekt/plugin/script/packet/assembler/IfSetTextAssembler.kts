package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeIntLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfSetTextPacket>(opcode = 17, size = -2) {
    buildPacket {
        writeStringCp1252NullTerminated(text)
        writeIntLittleEndian(packedInterface)
    }
}

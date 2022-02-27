package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.SetPlayerOpPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<SetPlayerOpPacket>(opcode = 41, size = -1) {
    buildPacket {
        writeByteAdd(priority.toByte())
        writeByteAdd(index.toByte())
        writeStringCp1252NullTerminated(option)
    }
}

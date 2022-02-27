package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetHiddenPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeIntV1
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetHiddenPacket>(opcode = 57, size = 5) {
    buildPacket {
        writeByte(hidden.toByte())
        writeIntV1(packedInterface)
    }
}

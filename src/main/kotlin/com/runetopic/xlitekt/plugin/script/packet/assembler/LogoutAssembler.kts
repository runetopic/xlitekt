package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.LogoutPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LogoutPacket>(opcode = 20, size = 1) {
    buildPacket {
        writeByte(type.toByte())
    }
}

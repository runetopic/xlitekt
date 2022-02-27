package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.MiniMapTogglePacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<MiniMapTogglePacket>(opcode = 36, size = 1) {
    buildPacket {
        writeByte(type.toByte())
    }
}

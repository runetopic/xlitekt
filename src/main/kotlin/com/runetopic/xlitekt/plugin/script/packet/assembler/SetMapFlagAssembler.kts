package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.SetMapFlagPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SetMapFlagPacket>(opcode = 93, size = 2) {
    buildPacket {
        writeByte(destinationX.toByte())
        writeByte(destinationZ.toByte())
    }
}

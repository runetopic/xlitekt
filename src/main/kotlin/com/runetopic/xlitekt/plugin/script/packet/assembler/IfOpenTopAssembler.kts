package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenTopPacket>(opcode = 31, size = 2) {
    buildPacket {
        writeShort(interfaceId.toShort())
    }
}

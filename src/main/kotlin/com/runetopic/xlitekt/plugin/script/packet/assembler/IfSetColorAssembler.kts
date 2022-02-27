package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetColorPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeIntV2
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfSetColorPacket>(opcode = 62, size = 6) {
    buildPacket {
        writeIntV2(packedInterface)
        writeShortLittleEndian(color.toShort())
    }
}

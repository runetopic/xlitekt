package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfOpenSubPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeByteSubtract
import com.runetopic.xlitekt.shared.toByte
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenSubPacket>(opcode = 2, size = 7) {
    buildPacket {
        writeShortLittleEndian(interfaceId.toShort())
        writeByteSubtract(alwaysOpen.toByte())
        writeInt(toPackedInterface)
    }
}

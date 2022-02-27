package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<VarpSmallPacket>(opcode = 94, size = 3) {
    buildPacket {
        writeByteAdd(value.toByte())
        writeShort(id.toShort())
    }
}

package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpHeldPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import com.runetopic.xlitekt.shared.buffer.readIntV2
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndianSubtract
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 43, size = 8) {
    OpHeldPacket(
        index = 5,
        fromPackedInterface = readIntV2(),
        fromSlotId = readUShortLittleEndianSubtract(),
        fromItemId = readUShort().toInt(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}

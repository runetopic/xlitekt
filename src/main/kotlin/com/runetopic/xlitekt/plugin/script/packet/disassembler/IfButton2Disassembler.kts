package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.IfButtonPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 11, size = 8) {
    IfButtonPacket(
        index = 2,
        packedInterface = readInt(),
        slotId = readUShort().toInt(),
        itemId = readUShort().toInt()
    )
}

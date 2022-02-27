package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.OpObjPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndian

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 100, size = 2) {
    OpObjPacket(
        index = 6,
        packedInterface = -1,
        slotId = 0xffff,
        itemId = readUShortLittleEndian()
    )
}

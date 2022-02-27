package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.CloseModalPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 96, size = 0) {
    CloseModalPacket()
}

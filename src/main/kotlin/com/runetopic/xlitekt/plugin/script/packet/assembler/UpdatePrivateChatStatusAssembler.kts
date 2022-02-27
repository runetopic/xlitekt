package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdatePrivateChatStatusPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdatePrivateChatStatusPacket>(opcode = 85, size = 1) {
    buildPacket {
        writeByte(mode.toByte())
    }
}

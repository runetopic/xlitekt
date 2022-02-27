package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdatePublicChatStatusPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.shared.buffer.writeByteNegate
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    buildPacket {
        writeByte(chatMode.toByte())
        writeByteNegate(tradeMode.toByte())
    }
}

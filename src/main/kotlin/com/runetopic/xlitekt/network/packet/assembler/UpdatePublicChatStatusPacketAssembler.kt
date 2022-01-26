package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdatePublicChatStatusPacket
import com.runetopic.xlitekt.util.ext.writeByteNegate

class UpdatePublicChatStatusPacketAssembler : PacketAssembler<UpdatePublicChatStatusPacket>(opcode = 32, size = 2) {
    override fun assemblePacket(packet: UpdatePublicChatStatusPacket) = buildPacket {
        writeByte(packet.chatMode.toByte())
        writeByteNegate(packet.tradeMode.toByte())
    }
}

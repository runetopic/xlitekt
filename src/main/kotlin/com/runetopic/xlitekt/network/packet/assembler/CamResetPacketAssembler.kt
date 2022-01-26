package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetTextPacket

/**
 * @author Jordan Abraham
 */
class CamResetPacketAssembler : PacketAssembler<IfSetTextPacket>(opcode = 35, size = 0) {
    override fun assemblePacket(packet: IfSetTextPacket) = buildPacket {}
}

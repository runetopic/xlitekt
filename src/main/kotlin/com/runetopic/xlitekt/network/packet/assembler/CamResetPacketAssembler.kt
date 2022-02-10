package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.CamResetPacket
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 */
class CamResetPacketAssembler : PacketAssembler<CamResetPacket>(opcode = 35, size = 0) {
    override fun assemblePacket(packet: CamResetPacket) = buildPacket {}
}

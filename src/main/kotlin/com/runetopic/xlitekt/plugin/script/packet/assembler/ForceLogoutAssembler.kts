package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.ForceLogoutPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<ForceLogoutPacket>(opcode = 87, size = 0) {
    buildPacket { }
}

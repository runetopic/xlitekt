package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.UpdateWeightPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateWeightPacket>(opcode = 50, size = -2) {
    buildPacket {
        writeShort(weight)
    }
}

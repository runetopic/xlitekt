package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.SoundEffectPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    buildPacket {
        writeShort(id.toShort())
        writeByte(count.toByte())
        writeShort(delay.toShort())
    }
}

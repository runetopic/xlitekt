package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.SoundEffectPacket
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort

/**
 * @author Tyler Telis
 */
class SoundEffectPacketAssembler : PacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    override fun assemblePacket(packet: SoundEffectPacket) = buildPacket {
        writeShort(packet.id.toShort())
        writeByte(packet.count.toByte())
        writeShort(packet.delay.toShort())
    }
}

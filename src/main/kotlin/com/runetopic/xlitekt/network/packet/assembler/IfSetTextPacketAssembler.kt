package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.IfSetTextPacket
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeIntLittleEndian

/**
 * @author Jordan Abraham
 */
class IfSetTextPacketAssembler : PacketAssembler<IfSetTextPacket>(opcode = 17, size = -2) {
    override fun assemblePacket(packet: IfSetTextPacket) = buildPacket {
        writeStringCp1252NullTerminated(packet.text)
        writeIntLittleEndian(packet.packedInterface)
    }
}

package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.SetPlayerOpPacket
import com.runetopic.xlitekt.shared.buffer.toByte
import com.runetopic.xlitekt.shared.buffer.writeByteAdd
import com.runetopic.xlitekt.shared.buffer.writeStringCp1252NullTerminated
import io.ktor.utils.io.core.buildPacket

/**
 * @author Tyler Telis
 */
class SetPlayerOpPacketAssembler : PacketAssembler<SetPlayerOpPacket>(opcode = 41, size = -1) {
    override fun assemblePacket(packet: SetPlayerOpPacket) = buildPacket {
        writeByteAdd(packet.priority.toByte())
        writeByteAdd(packet.index.toByte())
        writeStringCp1252NullTerminated(packet.option)
    }
}

package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.SetPlayerOpPacket
import com.runetopic.xlitekt.util.ext.toByte
import com.runetopic.xlitekt.util.ext.writeByteAdd
import com.runetopic.xlitekt.util.ext.writeStringCp1252NullTerminated

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

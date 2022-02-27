package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.IfMoveSubPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    buildPacket {
        writeInt(fromPackedInterface)
        writeInt(toPackedInterface)
    }
}

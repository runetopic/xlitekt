package com.runetopic.xlitekt.plugin.script.packet.disassembler

import com.runetopic.xlitekt.network.packet.MovementPacket
import com.runetopic.xlitekt.network.packet.disassembler.onPacketDisassembler
import com.runetopic.xlitekt.shared.buffer.readUShortAdd
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndianAdd
import io.ktor.utils.io.core.readUByte

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketDisassembler(opcode = 57, size = -1) {
    MovementPacket(
        movementType = readUByte().toInt(),
        destinationX = readUShortLittleEndianAdd(),
        destinationZ = readUShortAdd()
    )
}

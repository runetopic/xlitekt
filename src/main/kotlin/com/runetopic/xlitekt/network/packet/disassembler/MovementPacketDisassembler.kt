package com.runetopic.xlitekt.network.packet.disassembler

import com.runetopic.xlitekt.network.packet.MovementPacket
import com.runetopic.xlitekt.shared.buffer.readUShortAdd
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndianAdd
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte

class MovementPacketDisassembler : PacketDisassembler<MovementPacket>(opcode = 57, size = -1) {
    override fun disassemblePacket(packet: ByteReadPacket): MovementPacket = MovementPacket(
        movementType = packet.readUByte().toInt(),
        destinationX = packet.readUShortLittleEndianAdd(),
        destinationZ = packet.readUShortAdd(),
    )
}

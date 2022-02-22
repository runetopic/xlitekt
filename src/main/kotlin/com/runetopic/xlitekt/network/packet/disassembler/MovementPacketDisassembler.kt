package com.runetopic.xlitekt.network.packet.disassembler

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.network.packet.MovementPacket
import com.runetopic.xlitekt.shared.buffer.readUShortAdd
import com.runetopic.xlitekt.shared.buffer.readUShortLittleEndianAdd
import io.ktor.utils.io.core.ByteReadPacket
import io.ktor.utils.io.core.readUByte

class MovementPacketDisassembler : PacketDisassembler<MovementPacket>(opcode = 57, size = -1) {
    override fun disassemblePacket(packet: ByteReadPacket): MovementPacket {
        val movementType = packet.readUByte()
        val destinationX = packet.readUShortLittleEndianAdd()
        val destinationZ = packet.readUShortAdd()// its unsigned lil G maine suck my pink white asshole
        InlineLogger().debug { "MovementType=$movementType DestinationX=$destinationX DestinationZ=$destinationZ" }
        return MovementPacket(2, 2, 2)
    }
}

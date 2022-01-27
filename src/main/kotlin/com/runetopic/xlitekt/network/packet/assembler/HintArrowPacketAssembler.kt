package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.game.actor.HintArrowType
import com.runetopic.xlitekt.network.packet.HintArrowPacket
import io.ktor.utils.io.core.fill
import io.ktor.utils.io.core.writeShort

class HintArrowPacketAssembler : PacketAssembler<HintArrowPacket>(opcode = 43, size = 6) {
    override fun assemblePacket(packet: HintArrowPacket) = buildPacket {
        writeByte(packet.type.id.toByte())

        when (packet.type) {
            HintArrowType.PLAYER, HintArrowType.NPC -> {
                writeShort(packet.targetIndex.toShort())
                fill(3, 0)
            }
            HintArrowType.LOCATION -> {
                writeShort(packet.targetX.toShort())
                writeShort(packet.targetZ.toShort())
                writeByte(packet.targetHeight.toByte())
            }
        }
    }
}

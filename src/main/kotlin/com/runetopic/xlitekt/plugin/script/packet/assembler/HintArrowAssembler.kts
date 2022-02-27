package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.game.actor.render.HintArrowType
import com.runetopic.xlitekt.network.packet.HintArrowPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.fill
import io.ktor.utils.io.core.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<HintArrowPacket>(opcode = 43, size = 6) {
    buildPacket {
        writeByte(type.id.toByte())

        when (type) {
            HintArrowType.PLAYER, HintArrowType.NPC -> {
                writeShort(targetIndex.toShort())
                fill(3, 0)
            }
            HintArrowType.LOCATION -> {
                writeShort(targetX.toShort())
                writeShort(targetZ.toShort())
                writeByte(targetHeight.toByte())
            }
        }
    }
}

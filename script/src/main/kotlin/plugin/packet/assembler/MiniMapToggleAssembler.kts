package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.MiniMapTogglePacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<MiniMapTogglePacket>(opcode = 36, size = 1) {
    buildPacket {
        writeByte(type.toByte())
    }
}

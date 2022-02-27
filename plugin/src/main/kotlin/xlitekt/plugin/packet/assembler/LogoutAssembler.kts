package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.LogoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<LogoutPacket>(opcode = 20, size = 1) {
    buildPacket {
        writeByte(type.toByte())
    }
}

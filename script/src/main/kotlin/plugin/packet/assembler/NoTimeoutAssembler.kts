package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.NoTimeoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<NoTimeoutPacket>(opcode = 58, size = 0) {
    buildPacket { }
}

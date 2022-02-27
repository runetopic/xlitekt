package xlitekt.plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.ForceLogoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<ForceLogoutPacket>(opcode = 87, size = 0) {
    buildPacket { }
}

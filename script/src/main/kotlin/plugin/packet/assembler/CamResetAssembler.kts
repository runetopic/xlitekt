package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<CamResetPacket>(opcode = 35, size = 0) {
    buildPacket { }
}

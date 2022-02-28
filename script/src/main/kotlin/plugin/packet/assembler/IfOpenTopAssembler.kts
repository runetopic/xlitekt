package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeShort
import xlitekt.game.packet.IfOpenTopPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<IfOpenTopPacket>(opcode = 31, size = 2) {
    buildPacket {
        writeShort(interfaceId.toShort())
    }
}

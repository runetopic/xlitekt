package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import xlitekt.game.packet.IfCloseSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfCloseSubPacket>(opcode = 13, size = 4) {
    buildPacket {
        writeInt(packedInterface)
    }
}

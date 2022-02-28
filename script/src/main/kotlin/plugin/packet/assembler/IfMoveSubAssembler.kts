package plugin.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import xlitekt.game.packet.IfMoveSubPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<IfMoveSubPacket>(opcode = 30, size = 8) {
    buildPacket {
        writeInt(fromPackedInterface)
        writeInt(toPackedInterface)
    }
}

package plugin.packet.disassembler

import io.ktor.utils.io.core.readInt
import io.ktor.utils.io.core.readUShort
import xlitekt.game.packet.IfButtonPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 11, size = 8) {
    IfButtonPacket(
        index = 2,
        packedInterface = readInt(),
        slotId = readUShort().toInt(),
        itemId = readUShort().toInt()
    )
}

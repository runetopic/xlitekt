package script.packet.disassembler

import io.ktor.utils.io.core.readInt
import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortAdd

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 66, size = 8) {
    OpHeldPacket(
        index = 3,
        fromSlotId = readUShortAdd(),
        fromItemId = readUShortAdd(),
        fromPackedInterface = readInt(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}

package script.packet.disassembler

import io.ktor.utils.io.core.readInt
import xlitekt.game.packet.OpHeldPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readInt
import xlitekt.shared.buffer.readUShortAdd

/**
 * @author Justin Kenney
 */
onPacketDisassembler(opcode = 72, size = 8) {
    OpHeldPacket(
        index = 1,
        fromPackedInterface = readInt(),
        fromSlotId = readUShortAdd(),
        fromItemId = readUShortAdd(),
        toPackedInterface = -1,
        toSlotId = 0xffff,
        toItemId = 0xffff
    )
}

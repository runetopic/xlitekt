package script.packet.disassembler

import io.ktor.utils.io.core.readShortLittleEndian
import xlitekt.game.packet.OpLocPacket
import xlitekt.game.packet.disassembler.onPacketDisassembler
import xlitekt.shared.buffer.readUShortAdd
import xlitekt.shared.buffer.readUShortLittleEndian
import xlitekt.shared.toBoolean

onPacketDisassembler(opcode = 94, size = 7) {
    OpLocPacket(
        index = 2,
        z = readShortLittleEndian().toInt(),
        running = readByte().toInt().toBoolean(),
        x = readUShortAdd(),
        objectId = readUShortLittleEndian()
    )
}

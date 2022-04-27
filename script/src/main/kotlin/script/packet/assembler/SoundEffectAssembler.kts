package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.SoundEffectPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeShort

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<SoundEffectPacket>(opcode = 81, size = 5) {
    buildPacket {
        writeShort { id }
        writeByte { count }
        writeShort { delay }
    }
}

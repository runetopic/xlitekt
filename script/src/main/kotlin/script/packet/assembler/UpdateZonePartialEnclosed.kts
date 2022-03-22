package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeBytes

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZonePartialEnclosedPacket>(opcode = 56, size = -2) {
    buildPacket {
        writeBytes(packet.copy().readBytes())
    }
}

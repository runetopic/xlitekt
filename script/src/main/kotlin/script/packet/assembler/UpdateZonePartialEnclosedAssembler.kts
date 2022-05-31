package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZonePartialEnclosedPacket>(opcode = 56, size = -2) {
    buildPacket {
        writeByteNegate { (localZ shr 3) shl 3 }
        writeByte { (localX shr 3) shl 3 }
        writeBytes { bytes }
    }
}

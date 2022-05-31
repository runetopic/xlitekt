package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import xlitekt.game.packet.UpdateZonePartialFollowsPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteAdd
import xlitekt.shared.buffer.writeByteSubtract

/**
 * @author Jordan Abraham
 */
onPacketAssembler<UpdateZonePartialFollowsPacket>(opcode = 53, size = 2) {
    buildPacket {
        writeByte { (localZ shr 3) shl 3 }
        writeByteSubtract { (localX shr 3) shl 3 }
    }
}

package script.packet.assembler

import xlitekt.game.packet.UpdateZonePartialFollowsPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteSubtract
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateZonePartialFollowsPacket>(opcode = 53, size = 2) {
    it.writeByte((localZ shr 3) shl 3)
    it.writeByteSubtract((localX shr 3) shl 3)
}

package script.packet.assembler

import xlitekt.game.packet.UpdateZonePartialEnclosedPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeByte
import xlitekt.shared.buffer.writeByteNegate
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateZonePartialEnclosedPacket>(opcode = 56, size = -2) {
    it.writeByteNegate((localZ shr 3) shl 3)
    it.writeByte((localX shr 3) shl 3)
    it.writeBytes(bytes)
}

package script.packet.assembler

import xlitekt.game.packet.UpdateRebootTimerPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    it.writeShort(rebootTimer)
}

package script.packet.assembler

import xlitekt.game.packet.UpdateRebootTimerPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<UpdateRebootTimerPacket>(opcode = 15, size = 2) {
    it.writeShort(rebootTimer)
}

package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketHandlerListener>().handlePacket<CloseModalPacket> {
    player.interfaces.closeModal()
    player.cancelAll()
}

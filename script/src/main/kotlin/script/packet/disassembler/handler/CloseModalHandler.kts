package script.packet.disassembler.handler

import xlitekt.game.actor.cancelAll
import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketHandlerListener>().handlePacket<CloseModalPacket> {
    player.interfaces.closeModal()
    player.cancelAll()
}

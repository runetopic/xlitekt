package script.packet.disassembler.handler

import xlitekt.game.actor.cancelWeak
import xlitekt.game.packet.CloseModalPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

/**
 * @author Jordan Abraham
 */
onPacketHandler<CloseModalPacket> {
    player.interfaces.closeModal()
    player.cancelWeak()
}

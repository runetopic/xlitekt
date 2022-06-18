package script.packet.disassembler.handler

import xlitekt.game.content.command.CommandListener
import xlitekt.game.packet.ClientCheatPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.inject
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
val commandListener by inject<CommandListener>()

insert<PacketHandlerListener>().handlePacket<ClientCheatPacket> {
    commandListener.execute(packet.command, player)
}

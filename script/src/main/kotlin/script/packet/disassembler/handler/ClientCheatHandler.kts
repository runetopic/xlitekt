package script.packet.disassembler.handler

import xlitekt.game.content.command.Commands
import xlitekt.game.packet.ClientCheatPacket
import xlitekt.game.packet.disassembler.handler.PacketHandlerListener
import xlitekt.shared.insert

insert<PacketHandlerListener>().handlePacket<ClientCheatPacket> { Commands.execute(this.packet.command, player) }

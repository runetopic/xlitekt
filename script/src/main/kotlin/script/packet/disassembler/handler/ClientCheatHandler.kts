package script.packet.disassembler.handler

import xlitekt.game.content.command.Commands
import xlitekt.game.packet.ClientCheatPacket
import xlitekt.game.packet.disassembler.handler.onPacketHandler

onPacketHandler<ClientCheatPacket> { Commands.execute(this.packet.command, player) }

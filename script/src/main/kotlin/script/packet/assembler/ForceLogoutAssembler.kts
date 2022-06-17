package script.packet.assembler

import xlitekt.game.packet.ForceLogoutPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
insert<PacketAssemblerListener>().assemblePacket<ForceLogoutPacket>(opcode = 87, size = 0) {}

package script.packet.assembler

import xlitekt.game.packet.ForceLogoutPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
lazyInject<PacketAssemblerListener>().assemblePacket<ForceLogoutPacket>(opcode = 87, size = 0) {}

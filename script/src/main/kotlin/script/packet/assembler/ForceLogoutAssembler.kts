package script.packet.assembler

import xlitekt.game.packet.ForceLogoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
onPacketAssembler<ForceLogoutPacket>(opcode = 87, size = 0) {}

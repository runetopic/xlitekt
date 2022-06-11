package script.packet.assembler

import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<CamResetPacket>(opcode = 35, size = 0) {}

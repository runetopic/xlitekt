package script.packet.assembler

import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<CamResetPacket>(opcode = 35, size = 0) {}

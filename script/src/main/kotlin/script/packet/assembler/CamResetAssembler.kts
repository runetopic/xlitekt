package script.packet.assembler

import xlitekt.game.packet.CamResetPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<CamResetPacket>(opcode = 35, size = 0) {}

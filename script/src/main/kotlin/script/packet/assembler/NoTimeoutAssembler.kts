package script.packet.assembler

import xlitekt.game.packet.NoTimeoutPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
insert<PacketAssemblerListener>().assemblePacket<NoTimeoutPacket>(opcode = 58, size = 0) {}

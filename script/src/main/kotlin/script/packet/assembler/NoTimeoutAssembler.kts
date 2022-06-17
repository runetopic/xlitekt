package script.packet.assembler

import xlitekt.game.packet.NoTimeoutPacket
import xlitekt.game.packet.assembler.PacketAssemblerListener
import xlitekt.shared.lazyInject

/**
 * @author Jordan Abraham
 */
lazyInject<PacketAssemblerListener>().assemblePacket<NoTimeoutPacket>(opcode = 58, size = 0) {}

package script.packet.assembler

import xlitekt.game.packet.NoTimeoutPacket
import xlitekt.game.packet.assembler.onPacketAssembler

/**
 * @author Jordan Abraham
 */
onPacketAssembler<NoTimeoutPacket>(opcode = 58, size = 0) {}

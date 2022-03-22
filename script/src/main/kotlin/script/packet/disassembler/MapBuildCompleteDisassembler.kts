import xlitekt.game.packet.MapBuildCompletePacket
import xlitekt.game.packet.disassembler.onPacketDisassembler

/**
 * @author Jordan Abraham
 */
onPacketDisassembler(opcode = 55, size = 0) {
    MapBuildCompletePacket()
}

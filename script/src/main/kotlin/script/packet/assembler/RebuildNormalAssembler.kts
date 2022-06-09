package script.packet.assembler

import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.allocateDynamic
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeInt
import xlitekt.shared.buffer.writeShort
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.buffer.writeShortLittleEndian
import xlitekt.shared.inject
import xlitekt.shared.resource.MapSquares

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
private val mapSquares by inject<MapSquares>()

onPacketAssembler<RebuildNormalPacket>(opcode = 54, size = -2) {
    allocateDynamic(7500) {
        if (update) {
            viewport.init(this, players)
        }

        val zoneX = location.zoneX
        val zoneZ = location.zoneZ

        writeShortAdd(zoneZ)
        writeShortLittleEndian(zoneX)

        var size = 0
        val xteas = allocateDynamic(256) {
            ((zoneX - 6) / 8..(zoneX + 6) / 8).forEach { x ->
                ((zoneZ - 6) / 8..(zoneZ + 6) / 8).forEach { y ->
                    val regionId = y + (x shl 8)
                    val xteaKeys = mapSquares[regionId]?.key ?: listOf(0, 0, 0, 0)
                    xteaKeys.forEach(::writeInt)
                    ++size
                }
            }
        }

        writeShort(size)
        writeBytes(xteas)
    }
}

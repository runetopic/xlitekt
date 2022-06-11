package script.packet.assembler

import io.ktor.utils.io.core.writeInt
import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.buildDynamicPacket
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
    buildDynamicPacket {
        if (initialize) {
            viewport.init(this, players)
        }

        val zoneX = location.zoneX
        val zoneZ = location.zoneZ

        writeShortAdd(zoneZ)
        writeShortLittleEndian(zoneX)

        val regionsX = ((zoneX - 6) / 8..(zoneX + 6) / 8)
        val regionsZ = ((zoneZ - 6) / 8..(zoneZ + 6) / 8)

        writeShort(regionsX.count() * regionsZ.count())

        for (x in regionsX) {
            for (z in regionsZ) {
                val regionId = z + (x shl 8)
                val xteaKeys = mapSquares[regionId]?.key ?: listOf(0, 0, 0, 0)
                for (key in xteaKeys) {
                    writeInt(key)
                }
            }
        }
    }
}

package script.packet.assembler

import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian
import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.assembler.onPacketAssembler
import xlitekt.shared.buffer.writeBytes
import xlitekt.shared.buffer.writeShortAdd
import xlitekt.shared.inject
import xlitekt.shared.resource.MapSquares

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
private val mapSquares by inject<MapSquares>()

onPacketAssembler<RebuildNormalPacket>(opcode = 54, size = -2) {
    buildPacket {
        if (update) {
            viewport.init(this)
        }

        val zoneX = location.zoneX
        val zoneZ = location.zoneZ

        writeShortAdd(zoneX.toShort())
        writeShortLittleEndian(zoneZ.toShort())

        var size = 0
        val xteas = buildPacket {
            ((zoneX - 6) / 8..(zoneX + 6) / 8).forEach { x ->
                ((zoneZ - 6) / 8..(zoneZ + 6) / 8).forEach { y ->
                    val regionId = y + (x shl 8)
                    val xteaKeys = mapSquares.find { it.mapsquare == regionId }?.key ?: listOf(0, 0, 0, 0)
                    xteaKeys.forEach(::writeInt)
                    ++size
                }
            }
        }

        writeShort(size.toShort())
        writeBytes(xteas.readBytes())
    }
}

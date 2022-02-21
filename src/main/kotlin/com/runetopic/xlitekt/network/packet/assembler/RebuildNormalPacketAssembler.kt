package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.writeShortAdd
import com.runetopic.xlitekt.shared.resource.MapSquares
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class RebuildNormalPacketAssembler : PacketAssembler<RebuildNormalPacket>(opcode = 54, size = -2) {

    private val mapSquares by inject<MapSquares>()

    override fun assemblePacket(packet: RebuildNormalPacket) = buildPacket {
        if (packet.update) {
            packet.viewport.init(this@buildPacket)
        }

        val zoneX = packet.location.zoneX
        val zoneZ = packet.location.zoneZ

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
        writePacket(xteas)
    }
}

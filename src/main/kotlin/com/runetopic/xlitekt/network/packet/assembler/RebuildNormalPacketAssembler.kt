package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.writeShortAdd
import com.runetopic.xlitekt.util.resource.MapSquare
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian

/**
 * @author Tyler Telis
 */
class RebuildNormalPacketAssembler : PacketAssembler<RebuildNormalPacket>(opcode = 54, size = -2) {

    private val mapSquares by inject<List<MapSquare>>()

    override fun assemblePacket(packet: RebuildNormalPacket) = buildPacket {
        if (packet.update) {
            packet.viewport.init(this@buildPacket)
        }

        val chunkX = packet.tile.chunkX
        val chunkZ = packet.tile.chunkZ

        writeShortAdd(chunkX.toShort())
        writeShortLittleEndian(chunkZ.toShort())

        var size = 0
        var forceSend = false

        if ((chunkX / 8 == 48 || chunkX / 8 == 49) && chunkZ / 8 == 48) {
            forceSend = true
        }
        if (chunkX / 8 == 48 && chunkZ / 8 == 148) {
            forceSend = true
        }

        val xteas = buildPacket {
            for (x in (chunkX - 6) / 8..(chunkX + 6) / 8) {
                for (y in (chunkZ - 6) / 8..(chunkZ + 6) / 8) {
                    val regionId = y + (x shl 8)
                    if (!forceSend || y != 49 && y != 149 && y != 147 && x != 50 && (x != 49 || y != 47)) {
                        val xteaKeys = mapSquares.find { it.regionId == regionId }?.keys ?: listOf(0, 0, 0, 0)
                        xteaKeys.forEach { writeInt(it) }
                        ++size
                    }
                }
            }
        }

        writeShort(size.toShort())
        writePacket(xteas.build())
    }
}

package com.runetopic.xlitekt.network.packet.assembler

import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.writeShortAdd
import com.runetopic.xlitekt.util.resource.MapSquares
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

        val chunkX = packet.tile.chunkX
        val chunkZ = packet.tile.chunkZ

        writeShortAdd(chunkX.toShort())
        writeShortLittleEndian(chunkZ.toShort())

        var size = 0
        val xteas = buildPacket {
            ((chunkX - 6) / 8..(chunkX + 6) / 8).forEach { x ->
                ((chunkZ - 6) / 8..(chunkZ + 6) / 8).forEach { y ->
                    val regionId = y + (x shl 8)
                    val xteaKeys = mapSquares.find { it.regionId == regionId }?.keys ?: listOf(0, 0, 0, 0)
                    xteaKeys.forEach(::writeInt)
                    ++size
                }
            }
        }

        writeShort(size.toShort())
        writePacket(xteas)
    }
}

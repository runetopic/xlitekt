package com.runetopic.xlitekt.network.packet.write

import com.runetopic.xlitekt.fs.MapSquare
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.ext.withBitAccess
import com.runetopic.xlitekt.util.ext.writeShortAdd
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian

class RebuildNormalPacket(
    private val update: Boolean
) : Packet {
    private val tile = Tile(3222, 3222) // TODO this will come from player

    override fun opcode(): Int = 54
    override fun size(): Int = -2
    override fun builder() = buildPacket {
        if (update) {
            withBitAccess {
                writeBits(30, tile.coordinates)
                for (index in 1..2047) {
                    if (index == 1) continue
                    writeBits(18, 0)
                }
            }
        }

        val chunkX = tile.chunkX
        val chunkZ = tile.chunkZ

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
                        val xteaKeys = inject<List<MapSquare>>().value.find { it.regionId == regionId }?.keys ?: listOf(0, 0, 0, 0)
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

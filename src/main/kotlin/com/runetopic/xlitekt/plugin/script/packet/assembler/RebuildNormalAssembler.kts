package com.runetopic.xlitekt.plugin.script.packet.assembler

import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.network.packet.assembler.onPacketAssembler
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.writeBytes
import com.runetopic.xlitekt.shared.buffer.writeShortAdd
import com.runetopic.xlitekt.shared.resource.MapSquares
import io.ktor.utils.io.core.buildPacket
import io.ktor.utils.io.core.readBytes
import io.ktor.utils.io.core.writeInt
import io.ktor.utils.io.core.writeShort
import io.ktor.utils.io.core.writeShortLittleEndian

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

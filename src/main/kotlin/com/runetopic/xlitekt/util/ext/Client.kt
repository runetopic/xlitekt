package com.runetopic.xlitekt.util.ext

import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.event.WriteEvent
import io.ktor.utils.io.core.BytePacketBuilder
import io.ktor.utils.io.core.writeShortLittleEndian

val tile = Tile(3222, 3222)

suspend fun Client.sendRebuildNormalMap() {
    val builder = BytePacketBuilder()

    builder.withBitAccess {
        writeBits(30, tile.coordinates)
        for (index in 1..2047) {
            if (index == 1) continue
            writeBits(18, 0)
        }
    }

    builder.writeShortAdd(tile.chunkX)
    builder.writeShortLittleEndian(tile.chunkZ.toShort())
    println(tile.chunkX)
    println(tile.chunkZ)
    eventPipeline.write(this, WriteEvent.GameWriteEvent(54, -2, builder.build()))
}

package com.runetopic.xlitekt.game.map

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder

data class Viewport(
    val player: Player,
    val nsnFlags: IntArray = IntArray(2048),
    val coordinates: IntArray = IntArray(2048),
    val localIndexes: IntArray = IntArray(2048),
    val externalIndexes: IntArray = IntArray(2048),
    val localPlayers: Array<Player?> = arrayOfNulls(2048),
    var localIndexesSize: Int = 0,
    var externalIndexesSize: Int = 0
) {
    fun init(builder: BytePacketBuilder) = builder.withBitAccess {
        writeBits(30, player.tile.coordinates)
        localPlayers[player.pid] = player
        localIndexes[localIndexesSize++] = player.pid
        (1 until 2048).forEach {
            if (it == player.pid) return@forEach
            val otherRegionCoordinates = localPlayers[it]?.tile?.regionCoordinates ?: 0
            writeBits(18, otherRegionCoordinates)
            coordinates[it] = otherRegionCoordinates
            externalIndexes[externalIndexesSize++] = it
        }
    }

    fun shift() {
        localIndexesSize = 0
        externalIndexesSize = 0
        (1 until 2048).forEach {
            when (localPlayers[it]) {
                null -> externalIndexes[externalIndexesSize++] = it
                else -> localIndexes[localIndexesSize++] = it
            }
            nsnFlags[it] = (nsnFlags[it] shr 1)
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Viewport

        if (!nsnFlags.contentEquals(other.nsnFlags)) return false
        if (!coordinates.contentEquals(other.coordinates)) return false
        if (!localIndexes.contentEquals(other.localIndexes)) return false
        if (!externalIndexes.contentEquals(other.externalIndexes)) return false
        if (!localPlayers.contentEquals(other.localPlayers)) return false
        if (localIndexesSize != other.localIndexesSize) return false
        if (externalIndexesSize != other.externalIndexesSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nsnFlags.contentHashCode()
        result = 31 * result + coordinates.contentHashCode()
        result = 31 * result + localIndexes.contentHashCode()
        result = 31 * result + externalIndexes.contentHashCode()
        result = 31 * result + localPlayers.contentHashCode()
        result = 31 * result + localIndexesSize
        result = 31 * result + externalIndexesSize
        return result
    }
}

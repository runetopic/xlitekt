package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.game.world.World.Companion.MAX_PLAYERS
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.util.ext.withBitAccess
import io.ktor.utils.io.core.BytePacketBuilder
import java.util.LinkedList

class Viewport(
    val player: Player,
) {
    val nsnFlags = IntArray(MAX_PLAYERS)
    val coordinates = IntArray(MAX_PLAYERS)
    val localIndexes = IntArray(MAX_PLAYERS)
    val externalIndexes = IntArray(MAX_PLAYERS)
    val localPlayers = Array<Player?>(MAX_PLAYERS) { null }
    val localNPCs = LinkedList<NPC>()
    var localIndexesSize: Int = 0
    var externalIndexesSize: Int = 0

    private val world by inject<World>()

    fun init(builder: BytePacketBuilder) = builder.withBitAccess {
        writeBits(30, player.tile.coordinates)
        localPlayers[player.index] = player
        localIndexes[localIndexesSize++] = player.index
        (1 until MAX_PLAYERS).forEach {
            if (it == player.index) return@forEach
            val otherRegionCoordinates = world.players[it]?.tile?.regionCoordinates ?: 0
            writeBits(18, otherRegionCoordinates)
            coordinates[it] = otherRegionCoordinates
            externalIndexes[externalIndexesSize++] = it
        }
    }

    fun shift() {
        localIndexesSize = 0
        externalIndexesSize = 0
        (1 until MAX_PLAYERS).forEach {
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

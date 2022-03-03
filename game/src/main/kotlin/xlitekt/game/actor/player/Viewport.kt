package xlitekt.game.actor.player

import io.ktor.utils.io.core.BytePacketBuilder
import xlitekt.game.actor.npc.NPC
import xlitekt.game.world.World
import xlitekt.game.world.World.Companion.MAX_PLAYERS
import xlitekt.shared.buffer.withBitAccess
import xlitekt.shared.inject
import java.util.LinkedList

class Viewport(
    val player: Player,
) {
    val nsnFlags = IntArray(MAX_PLAYERS)
    val locations = IntArray(MAX_PLAYERS)
    val localIndexes = IntArray(MAX_PLAYERS)
    val externalIndexes = IntArray(MAX_PLAYERS)
    val localPlayers = Array<Player?>(MAX_PLAYERS) { null }
    val localNPCs = LinkedList<NPC>()
    var localIndexesSize: Int = 0
    var externalIndexesSize: Int = 0

    fun init(builder: BytePacketBuilder) = builder.withBitAccess {
        writeBits(30, player.location.packedLocation)
        localPlayers[player.index] = player
        localIndexes[localIndexesSize++] = player.index
        (1 until MAX_PLAYERS).forEach {
            if (it == player.index) return@forEach
            val otherRegionCoordinates = world.players[it]?.location?.regionLocation ?: 0
            writeBits(18, otherRegionCoordinates)
            locations[it] = otherRegionCoordinates
            externalIndexes[externalIndexesSize++] = it
        }
    }

    fun update() {
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
        if (!locations.contentEquals(other.locations)) return false
        if (!localIndexes.contentEquals(other.localIndexes)) return false
        if (!externalIndexes.contentEquals(other.externalIndexes)) return false
        if (!localPlayers.contentEquals(other.localPlayers)) return false
        if (localIndexesSize != other.localIndexesSize) return false
        if (externalIndexesSize != other.externalIndexesSize) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nsnFlags.contentHashCode()
        result = 31 * result + locations.contentHashCode()
        result = 31 * result + localIndexes.contentHashCode()
        result = 31 * result + externalIndexes.contentHashCode()
        result = 31 * result + localPlayers.contentHashCode()
        result = 31 * result + localIndexesSize
        result = 31 * result + externalIndexesSize
        return result
    }

    private companion object {
        val world by inject<World>()
    }
}

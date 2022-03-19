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
    val highDefinitions = IntArray(MAX_PLAYERS)
    val lowDefinitions = IntArray(MAX_PLAYERS)
    val players = Array<Player?>(MAX_PLAYERS) { null }
    val npcs = LinkedList<NPC>()
    var highDefinitionsCount = 0
    var lowDefinitionsCount = 0
    var localPlayersCount = 1

    fun init(builder: BytePacketBuilder) = builder.withBitAccess {
        writeBits(30, player.location.packedLocation)
        players[player.index] = player
        highDefinitions[highDefinitionsCount++] = player.index
        (1 until MAX_PLAYERS).forEach {
            if (it == player.index) return@forEach
            val otherRegionCoordinates = world.players[it]?.location?.regionLocation ?: 0
            writeBits(18, otherRegionCoordinates)
            locations[it] = otherRegionCoordinates
            lowDefinitions[lowDefinitionsCount++] = it
        }
    }

    fun update() {
        highDefinitionsCount = 0
        lowDefinitionsCount = 0
        (1 until MAX_PLAYERS).forEach {
            when (players[it]) {
                null -> lowDefinitions[lowDefinitionsCount++] = it
                else -> highDefinitions[highDefinitionsCount++] = it
            }
            nsnFlags[it] = (nsnFlags[it] shr 1)
        }
    }

    fun isNsn(index: Int): Boolean = nsnFlags[index] and 0x1 != 0
    fun setNsn(index: Int) {
        nsnFlags[index] = nsnFlags[index] or 2
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Viewport

        if (!nsnFlags.contentEquals(other.nsnFlags)) return false
        if (!locations.contentEquals(other.locations)) return false
        if (!highDefinitions.contentEquals(other.highDefinitions)) return false
        if (!lowDefinitions.contentEquals(other.lowDefinitions)) return false
        if (!players.contentEquals(other.players)) return false
        if (highDefinitionsCount != other.highDefinitionsCount) return false
        if (lowDefinitionsCount != other.lowDefinitionsCount) return false

        return true
    }

    override fun hashCode(): Int {
        var result = nsnFlags.contentHashCode()
        result = 31 * result + locations.contentHashCode()
        result = 31 * result + highDefinitions.contentHashCode()
        result = 31 * result + lowDefinitions.contentHashCode()
        result = 31 * result + players.contentHashCode()
        result = 31 * result + highDefinitionsCount
        result = 31 * result + lowDefinitionsCount
        return result
    }

    private companion object {
        val world by inject<World>()
    }
}

package xlitekt.game.world

import io.ktor.util.collections.ConcurrentList
import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.game.actor.NPCList
import xlitekt.game.actor.PlayerList
import xlitekt.game.actor.npc.NPC
import xlitekt.game.actor.player.Client
import xlitekt.game.actor.player.Player
import xlitekt.game.world.map.collision.CollisionMap
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zones
import xlitekt.shared.inject
import xlitekt.shared.resource.NPCSpawns
import java.util.concurrent.ConcurrentHashMap

class World(
    val players: PlayerList = PlayerList(MAX_PLAYERS),
    val npcs: NPCList = NPCList(MAX_NPCs)
) {
    private val loginRequests = ConcurrentHashMap<Player, Client>()
    private val logoutRequests = ConcurrentList<Player>()

    private val maps by inject<MapEntryTypeProvider>()
    private val npcSpawns by inject<NPCSpawns>()

    fun build() {
        // Apply collision map.
        maps.entries().forEach(CollisionMap::applyCollision)
        // Apply npc spawns.
        npcSpawns.forEach {
            val location = Location(it.x, it.z, it.level)
            val npc = NPC(it.id, location)
            npc.previousLocation = location
            spawn(npc)
        }
    }

    fun spawn(npc: NPC) {
        npcs.add(npc)
        Zones[npc.location]?.npcs?.add(npc)
    }

    fun requestLogin(player: Player, client: Client) {
        loginRequests[player] = client
    }

    fun requestLogout(player: Player) {
        logoutRequests += player
    }

    fun processLoginRequests() = loginRequests.entries.take(500).onEach {
        it.key.init(it.value)
    }.also(loginRequests.entries::removeAll)

    fun processLogoutRequests() = logoutRequests.take(MAX_PLAYERS).onEach(Player::logout).also(logoutRequests::removeAll)

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

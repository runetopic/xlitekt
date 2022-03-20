package xlitekt.game.world

import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.game.actor.NPCList
import xlitekt.game.actor.PlayerList
import xlitekt.game.actor.npc.NPC
import xlitekt.game.world.map.collision.CollisionMap
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.Zones
import xlitekt.shared.inject
import xlitekt.shared.resource.NPCSpawns

class World(
    val players: PlayerList = PlayerList(MAX_PLAYERS),
    val npcs: NPCList = NPCList(MAX_NPCs)
) {
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

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

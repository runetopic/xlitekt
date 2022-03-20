package xlitekt.game

import xlitekt.cache.provider.map.MapEntryTypeProvider
import xlitekt.game.actor.npc.NPC
import xlitekt.game.world.World
import xlitekt.game.world.engine.LoopTask
import xlitekt.game.world.map.collision.CollisionMap
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import xlitekt.shared.resource.NPCSpawns

class Game {
    private val loop = LoopTask()
    private val maps by inject<MapEntryTypeProvider>()
    private val world by inject<World>()
    private val npcSpawns by inject<NPCSpawns>()

    fun start() {
        maps.entries().forEach(CollisionMap::applyCollision)

        npcSpawns.forEach {
            val location = Location(it.x, it.z, it.level)
            val npc = NPC(it.id, location)
            npc.previousLocation = location
            world.addNPC(npc)
        }

        loop.start()
    }

    fun shutdown() {
        loop.shutdown()
    }
}

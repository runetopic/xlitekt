package xlitekt.game.actor.npc

import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.Actor
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import xlitekt.shared.lazy

private val npcEntryTypeProvider by inject<NPCEntryTypeProvider>()

class NPC(
    val id: Int,
    override var location: Location
) : Actor(location) {
    val entry = npcEntryTypeProvider.entryType(id)

    fun init() {
        previousLocation = location
        lazy<World>().zone(location)?.enterZone(this)
    }

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    override fun toString(): String = "NPC(id=$id, entry=$entry)"
}

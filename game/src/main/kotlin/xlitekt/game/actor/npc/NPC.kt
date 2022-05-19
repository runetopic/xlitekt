package xlitekt.game.actor.npc

import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.Actor
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

private val npcEntryTypeProvider by inject<NPCEntryTypeProvider>()

class NPC(
    val id: Int,
    override var location: Location
) : Actor(location) {
    val entry by lazy { npcEntryTypeProvider.entryType(id) }

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    override fun toString(): String = "NPC(id=$id, entry=$entry)"
}

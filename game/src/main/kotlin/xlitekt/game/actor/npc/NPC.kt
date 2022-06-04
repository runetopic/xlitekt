package xlitekt.game.actor.npc

import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.Actor
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject

private val npcEntryTypeProvider by inject<NPCEntryTypeProvider>()
private val eventBus by inject<EventBus>()

class NPC(
    val id: Int,
    override var location: Location
) : Actor(location) {
    val entry = npcEntryTypeProvider.entryType(id)

    fun init() {
        previousLocation = location
        zone().enterZone(this)
        eventBus.notify(Events.NPCSpawnEvent(this))
    }

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    override fun toString(): String = "NPC(id=$id, entry=$entry)"
}

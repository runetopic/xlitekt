package xlitekt.game.actor.npc

import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.Actor
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.game.world.map.Location
import xlitekt.shared.insert

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
class NPC(
    val id: Int,
    override var location: Location,
) : Actor(location) {
    inline val entry get() = insert<NPCEntryTypeProvider>().entryType(id)

    fun init() {
        previousLocation = location
        zone.enterZone(this)
        insert<EventBus>().notify(Events.NPCSpawnEvent(this))
    }

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    override fun toString(): String = "NPC(id=$id, entry=$entry)"
}

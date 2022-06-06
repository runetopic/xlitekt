package xlitekt.game.actor.npc

import xlitekt.cache.provider.config.npc.NPCEntryTypeProvider
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.prayer.Prayer
import xlitekt.game.actor.prayer.Prayers
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.game.world.map.Location
import xlitekt.shared.inject

/**
 * @author Tyler Telis
 * @author Jordan Abraham
 */
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

    override val prayer = Prayer(this)

    override fun toString(): String = "NPC(id=$id, entry=$entry)"

    private companion object {
        val npcEntryTypeProvider by inject<NPCEntryTypeProvider>()
        val eventBus by inject<EventBus>()
    }
}

inline fun NPC.switchPrayerById(prayerId: () -> Int): NPC = this.also {
    it.prayer.switchById(prayerId.invoke())
}

inline fun NPC.switchPrayer(prayer: () -> Prayers): NPC = this.also {
    it.prayer.switchById(prayer.invoke().id)
}

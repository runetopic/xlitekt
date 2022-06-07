package xlitekt.game.actor.prayer

import xlitekt.game.actor.Actor
import xlitekt.shared.resource.prayer.*

/**
 * @author Justin Kenney
 */
class Prayer(
    private val player: Actor,
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()
) {
    fun switch(prayer: Prayers) = this.switch(prayer.id)

    fun switch(prayerId: Int) = Prayers.info(prayerId)?.let { this.switch(it) }

    private fun switch(prayerInfo: PrayerInfoResource) {
//        if (player is Player && player.skills.levels[Skill.PRAYER.id] < prayer.requiredLevel) {
//            player.message { "Unlocked at ${prayer.requiredLevel}" }
//            return
//        }

        if (this.isActive(prayerInfo)) {
            // | Turning Prayer Off |
            this.deactivate(prayerInfo.prayerType)
        } else {
            // | Turning New Prayer On |

            // turn off conflicting prayers
            PrayerType.getConflictingTypes(prayerInfo.prayerType)?.let { this.deactivate(*it) }

            // turn on the new prayer
            this.activePrayerMap[prayerInfo.prayerType] = prayerInfo
            // TODO turn on varbit
        }

        println(activePrayerMap.values.mapNotNull { it?.name })
    }

    fun activate(prayer: Prayers) {
        if (!this.isActive(prayer)) this.switch(prayer)
    }

    fun deactivate(prayer: Prayers) {
        Prayers.info(prayer.id)?.let { if (this.isActive(it)) this.deactivate(it.prayerType) }
    }

    fun deactivate(vararg prayerTypes: PrayerType?) {
        prayerTypes.forEach {
            this.activePrayerMap[it ?: return@forEach] = null
            // TODO turn off varbit
        }
    }

    fun turnOff() {
        this.deactivate(*PrayerType.values())
    }

    fun reset() {
        // TODO turn off all prayer varbits
        Prayers.info().values.forEach {
//            it.varbit
        }
    }

    fun overheadIcon() {
        // TODO
//        this.activePrayerMap[PrayerType.UTILITY]
    }

    fun isActive(prayer: Prayers): Boolean {
        return Prayers.info(prayer.id).let { if (it != null) this.isActive(it) else false }
    }

    private fun isActive(prayerInfo: PrayerInfoResource): Boolean {
        val activePrayerByType = activePrayerMap[prayerInfo.prayerType]
        return activePrayerByType?.interfaceChildId == prayerInfo.interfaceChildId
    }

    fun drainRate(): Int = activePrayerMap.values.sumOf { it?.drainRate ?: 0 }
}

package xlitekt.game.actor.prayer

import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.prayerIcon
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.Skills
import xlitekt.shared.resource.prayer.PrayerIconType
import xlitekt.shared.resource.prayer.PrayerInfoResource
import xlitekt.shared.resource.prayer.PrayerType
import xlitekt.shared.resource.prayer.Prayers

/**
 * @author Justin Kenney
 */

class Prayer(
    private val player: Actor,
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()
) {
    fun process() {
        if (!this.isActive()) return

        if (this.player.currentHitpoints() <= 0 || (this.player is Player && this.player.vars[83] == 0)) {
            // todo toggle off quick prayers
            // todo toggle off prayers
            return
        }

        if (this.player is Player && this.player.skills.levels[Skill.PRAYER.id] == 0) {
            player.message { "You need to recharge your Prayer at an altar." }
            turnOff()
            return
        }

        if (player is Player) {
            player.message { "Ticking prayer" }
        }
    }

    fun switch(prayer: Prayers) = Prayers.info(prayer.prayerName)?.let { this.switch(it) }

    fun switch(prayerInfo: PrayerInfoResource) {

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

            this.activePrayerMap[prayerInfo.prayerType] = prayerInfo
            // TODO turn on varbit
//            if (this.player is Player) this.player.vars[prayerInfo.varbit] = 1

//            overhead icons
            prayerInfo.icon?.let { this.player.prayerIcon { it } }
        }

        println(activePrayerMap.values.mapNotNull { it?.name })
    }

    fun activate(prayer: Prayers) {
        if (!this.isActive(prayer)) this.switch(prayer)
    }

    fun deactivate(prayer: Prayers) {
        Prayers.info(prayer.prayerName)?.let { if (this.isActive(it)) this.deactivate(it.prayerType) }
    }

    fun deactivate(vararg prayerTypes: PrayerType?) {
//        if (this.player is Player) this.player.vars[83] = 0
        prayerTypes.forEach {
            if (it == null) return@forEach
            val current = this.activePrayerMap[it] ?: return@forEach

            current.icon?.let { this.player.prayerIcon { PrayerIconType.NONE } }
            if (this.player is Player) this.player.vars[current.varbit] = 0
            this.activePrayerMap[it] = null

//            this.activePrayerMap[it ?: return@forEach] = null
//            // TODO turn off varbit
//            if (this.player is Player) this.player.vars[prayerInfo.varbit] = 1
        }
    }

    fun turnOff() {
        this.deactivate(*PrayerType.values())
    }

    fun reset() {
        // TODO turn off all prayer varbits
        this.activePrayerMap.forEach { this.activePrayerMap[it.key] = null }
//        Prayers.info().values
    }

    fun isActive(): Boolean = this.activePrayerMap.values.find { it != null } != null

    fun isActive(prayer: Prayers): Boolean {
        return Prayers.info(prayer.prayerName)?.let { this.isActive(it) } ?: false
    }

    private fun isActive(prayerInfo: PrayerInfoResource): Boolean {
        val activePrayerByType = activePrayerMap[prayerInfo.prayerType]
        return activePrayerByType?.name == prayerInfo.name
    }

    fun drainRate(): Int = activePrayerMap.values.sumOf { it?.drainRate ?: 0 }
}

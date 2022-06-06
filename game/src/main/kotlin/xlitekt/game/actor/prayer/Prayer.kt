package xlitekt.game.actor.prayer

import xlitekt.game.actor.Actor
import xlitekt.shared.inject
import xlitekt.shared.resource.PrayerInfoMap
import xlitekt.shared.resource.PrayerInfoResource
import xlitekt.shared.resource.PrayerType
import xlitekt.shared.resource.PrayerType.Companion.getConflictingTypes

/**
 * @author Justin Kenney
 */
private val prayerInfoMap by inject<PrayerInfoMap>()

enum class Prayers(
    val id: Int
) {
    THICK_SKIN(5),
    BURST_OF_STRENGTH(6),
    CLARITY_OF_THOUGHT(7),
    ROCK_SKIN(8),
    SUPERHUMAN_STRENGTH(9),
    IMPROVED_REFLEXES(10),
    RAPID_RESTORE(11),
    RAPID_HEAL(12),
    PROTECT_ITEM(13),
    STEEL_SKIN(14),
    ULTIMATE_STRENGTH(15),
    INCREDIBLE_REFLEXES(16),
    PROTECT_FROM_MAGIC(17),
    PROTECT_FROM_MISSILES(18),
    PROTECT_FROM_MELEE(19),
    RETRIBUTION(20),
    REDEMPTION(21),
    SMITE(22),
    SHARP_EYE(23),
    MYSTIC_WILL(24),
    HAWK_EYE(25),
    MYSTIC_LORE(26),
    EAGLE_EYE(27),
    MYSTIC_MIGHT(28),
    CHIVALRY(29),
    PIETY(30),
    RIGOUR(31),
    AUGURY(32),
    PRESERVE(33);

    fun info(): PrayerInfoResource? = prayerInfoMap[this.id] ?: null.also { println("Prayer ${this.id} not found in resources!") }
}

class Prayer(
    private val player: Actor,
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()
) {
    fun switch(prayer: Prayers) = this.switch(prayer.id)

    fun switch(prayerId: Int) {
        val prayer = prayerInfoMap[prayerId] ?: return

//        if (player is Player && player.skills.levels[Skill.PRAYER.id] < prayer.requiredLevel) {
//            player.message { "Unlocked at ${prayer.requiredLevel}" }
//            return
//        }

        if (this.isActive(prayer)) {
            // | Turning Prayer Off |
            this.deactivate(prayer.prayerType)
        } else {
            // | Turning New Prayer On |

            // turn off conflicting prayers
            getConflictingTypes(prayer.prayerType)?.let { this.deactivate(*it) }

            // turn on the new prayer
            this.activePrayerMap[prayer.prayerType] = prayer
            // turn on varbit
        }

        println(activePrayerMap.values.mapNotNull { it?.name })
    }

    fun activate(prayer: Prayers) {
        if (!this.isActive(prayer)) this.switch(prayer)
    }

    fun deactivate(vararg prayers: Prayers) {
        val prayerTypes: Array<PrayerType?> = Array(prayers.size) { prayers[it].info()?.prayerType }
        this.deactivate(*prayerTypes)
    }

    fun deactivate(vararg prayerTypes: PrayerType?) {
        prayerTypes.forEach {
            if (it == null) return@forEach
            this.activePrayerMap[it] = null
            // turn off varbit
        }
    }

    fun turnOff() {
        this.deactivate(*PrayerType.values())
    }

    /**
     * Used if the varbits and activePrayerMap do not match
     */
    fun reset() {
        this.deactivate(*Prayers.values())
    }

    fun isActive(prayer: Prayers): Boolean {
        return prayer.info().let { if (it != null) this.isActive(it) else false }
    }

    private fun isActive(prayerInfo: PrayerInfoResource): Boolean {
        val activePrayerByType = activePrayerMap[prayerInfo.prayerType]
        return activePrayerByType?.interfaceChildId == prayerInfo.interfaceChildId
    }

    fun drainRate(): Int = activePrayerMap.values.sumOf { it?.drainRate ?: 0 }
}

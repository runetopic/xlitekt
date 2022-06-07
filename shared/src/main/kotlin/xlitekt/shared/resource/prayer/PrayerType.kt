package xlitekt.shared.resource.prayer

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
enum class PrayerType(val id: Int) {
    DEFENCE(0),
    ATTACK(1),
    STRENGTH(2),
    RANGE(3),
    MAGIC(4),
    UTILITY(5),
    MULTI_COMBAT(6),
    RAPID_RESTORE(7),
    RAPID_HEAL(8),
    PROTECT_ITEM(9),
    PRESERVE(10);

    companion object {
        private val conflictingPrayerTypes: Map<PrayerType, Array<PrayerType>> = mapOf(
            UTILITY to arrayOf(UTILITY),
            DEFENCE to arrayOf(MULTI_COMBAT, DEFENCE),
            ATTACK to arrayOf(RANGE, MAGIC, MULTI_COMBAT, ATTACK),
            STRENGTH to arrayOf(RANGE, MAGIC, MULTI_COMBAT, STRENGTH),
            RANGE to arrayOf(ATTACK, STRENGTH, MAGIC, MULTI_COMBAT, RANGE),
            MAGIC to arrayOf(ATTACK, STRENGTH, RANGE, MULTI_COMBAT, MAGIC),
            MULTI_COMBAT to arrayOf(DEFENCE, ATTACK, STRENGTH, RANGE, MAGIC, MULTI_COMBAT)
        )

        fun getConflictingTypes(type: PrayerType) = conflictingPrayerTypes[type]
    }
}

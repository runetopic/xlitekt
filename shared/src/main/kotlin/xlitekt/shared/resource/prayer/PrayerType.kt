package xlitekt.shared.resource.prayer

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
enum class PrayerType {
    DEFENCE,
    ATTACK,
    STRENGTH,
    RANGE,
    MAGIC,
    UTILITY,
    MULTI_COMBAT,
    RAPID_RESTORE,
    RAPID_HEAL,
    PROTECT_ITEM,
    PRESERVE;

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

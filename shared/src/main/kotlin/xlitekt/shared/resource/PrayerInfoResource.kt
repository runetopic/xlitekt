package xlitekt.shared.resource

import kotlinx.serialization.Serializable

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
        private val conflictingPrayerTypes: Map<PrayerType, List<PrayerType>> = mapOf(
            DEFENCE to listOf(MULTI_COMBAT, DEFENCE),
            ATTACK to listOf(RANGE, MAGIC, MULTI_COMBAT, ATTACK),
            STRENGTH to listOf(RANGE, MAGIC, MULTI_COMBAT, STRENGTH),
            RANGE to listOf(
                ATTACK,
                STRENGTH,
                MAGIC,
                MULTI_COMBAT,
                RANGE
            ),
            MAGIC to listOf(
                ATTACK,
                STRENGTH,
                RANGE,
                MULTI_COMBAT,
                MAGIC
            ),
            UTILITY to listOf(UTILITY),
            MULTI_COMBAT to listOf(
                DEFENCE,
                ATTACK,
                STRENGTH,
                RANGE,
                MAGIC,
                MULTI_COMBAT
            )
        )

        fun getConflictingTypes(type: PrayerType) = conflictingPrayerTypes[type]
    }
}

/**
 * @author Justin Kenney
 */
@Serializable
data class PrayerInfoResource(
    val interfaceChildId: Int,
    val name: String,
    val requiredLevel: Int,
    val drainRate: Int,
    val prayerType: PrayerType,
    val varbit: Int
)

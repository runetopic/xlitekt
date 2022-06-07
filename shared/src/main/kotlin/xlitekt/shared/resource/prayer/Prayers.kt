package xlitekt.shared.resource.prayer

import kotlinx.serialization.Serializable
import xlitekt.shared.inject
import xlitekt.shared.resource.PrayerInfoMap

/**
 * @author Justin Kenney
 */
@Serializable
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

    companion object {
        private val prayerInfoMap by inject<PrayerInfoMap>()

        fun info() = prayerInfoMap.toMap()

        fun info(prayerId: Int): PrayerInfoResource? = prayerInfoMap[prayerId] ?: null.also { println("Prayer $prayerId not found in resources!") }
    }
}

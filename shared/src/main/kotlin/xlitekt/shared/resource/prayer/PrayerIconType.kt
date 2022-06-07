package xlitekt.shared.resource.prayer

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
enum class PrayerIconType(
    val overheadId: Int
) {
    NONE(-1),
    PROTECT_FROM_MELEE(0),
    PROTECT_FROM_MISSILES(1),
    PROTECT_FROM_MAGIC(2),
    RETRIBUTION(3),
    SMITE(4),
    REDEMPTION(5);
}

package xlitekt.shared.resource.prayer

import kotlinx.serialization.Serializable

/**
 * @author Justin Kenney
 */
@Serializable
data class PrayerInfoResource(
    val name: String,
    val requiredLevel: Int,
    val drainRate: Int,
    val prayerType: PrayerType,
    val varbit: Int,
    val icon: PrayerIconType? = null
)
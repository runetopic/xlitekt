package xlitekt.shared.resource.prayer

import xlitekt.shared.inject
import xlitekt.shared.resource.PrayerInfoMap

/**
 * @author Justin Kenney
 */
enum class Prayers(
    val prayerName: String
) {
    THICK_SKIN("thick_skin"),
    BURST_OF_STRENGTH("burst_of_strength"),
    CLARITY_OF_THOUGHT("clarity_of_thought"),
    ROCK_SKIN("rock_skin"),
    SUPERHUMAN_STRENGTH("superhuman_strength"),
    IMPROVED_REFLEXES("improved_reflexes"),
    RAPID_RESTORE("rapid_restore"),
    RAPID_HEAL("rapid_heal"),
    PROTECT_ITEM("protect_item"),
    STEEL_SKIN("steel_skin"),
    ULTIMATE_STRENGTH("ultimate_strength"),
    INCREDIBLE_REFLEXES("incredible_reflexes"),
    PROTECT_FROM_MAGIC("protect_from_magic"),
    PROTECT_FROM_MISSILES("protect_from_missiles"),
    PROTECT_FROM_MELEE("protect_from_melee"),
    RETRIBUTION("retribution"),
    REDEMPTION("redemption"),
    SMITE("smite"),
    SHARP_EYE("sharp_eye"),
    MYSTIC_WILL("mystic_will"),
    HAWK_EYE("hawk_eye"),
    MYSTIC_LORE("mystic_lore"),
    EAGLE_EYE("eagle_eye"),
    MYSTIC_MIGHT("mystic_might"),
    CHIVALRY("chivalry"),
    PIETY("piety"),
    RIGOUR("rigour"),
    AUGURY("augury"),
    PRESERVE("preserve");

    companion object {
        private val prayerInfoMap by inject<PrayerInfoMap>()

        fun info(name: String): PrayerInfoResource? = prayerInfoMap[name] ?: null.also { println("Prayer $name was not found in resources.") }
    }
}

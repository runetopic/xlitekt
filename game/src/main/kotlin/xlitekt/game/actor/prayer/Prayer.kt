package xlitekt.game.actor.prayer

import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.content.skill.Skill
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
    PRESERVE(33)
}
// PrayerTypes.values
class Prayer(
    private val player: Actor,
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()
) {
    fun switchById(prayerId: Int) {
        val prayer = prayerInfoMap[prayerId] ?: run { // Should never happen as all prayers are handled
//            player.message { "Prayer $prayerId not handled yet" }
            return
        }

//        if (player is Player && player.skills.levels[Skill.PRAYER.id] < prayer.requiredLevel) {
//            player.message { "Unlocked at ${prayer.requiredLevel}" }
//            return
//        }

        val activePrayerByType = activePrayerMap[prayer.prayerType]
        val isActivePrayer = activePrayerByType?.interfaceChildId == prayer.interfaceChildId

        if (isActivePrayer) {
            // | Turning Prayer Off |
            turnOff(prayer.prayerType)
        } else {
            // | Turning New Prayer On |

            // turn off conflicting prayers
            getConflictingTypes(prayer.prayerType).let { if (it != null) turnOff(*it.toTypedArray()) }

            // turn on the new prayer
            turnOn(prayer)
        }

        println(activePrayerMap.values.filterNotNull().map { it.name })
    }

    private fun calculateDrainRate(): Int {
        return activePrayerMap.values.sumOf { it?.drainRate ?: 0 }
    }

    private fun turnOn(prayer: PrayerInfoResource) {
        activePrayerMap[prayer.prayerType] = prayer
//        println("Turned on ${prayer.name}")
        // turn on new prayer prayer.varbit
    }

    private fun turnOff(vararg prayerTypes: PrayerType) {
        prayerTypes.forEach {
            val prayer = activePrayerMap[it] ?: return@forEach
            activePrayerMap[it] = null
//            println("Turned off ${prayer.name}")
            // turn off varbit
        }
    }
}

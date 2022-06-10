package xlitekt.game.content.prayer

import xlitekt.game.actor.Actor
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.renderAppearance
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.getBaseLevel
import xlitekt.game.content.skill.setLevel
import xlitekt.game.content.skill.setLevelToBase
import xlitekt.game.world.map.GameObject
import xlitekt.shared.resource.prayer.PrayerIconType
import xlitekt.shared.resource.prayer.PrayerInfoResource
import xlitekt.shared.resource.prayer.PrayerType
import xlitekt.shared.resource.prayer.Prayers
import java.util.*
import kotlin.concurrent.timerTask
import kotlin.math.floor

/**
 * @author Justin Kenney
 */

class Prayer(
    private val actor: Actor
) {
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()

    private var activePrayers = 0
    private var activeDrainRate = 0
    private var drainCounter = 0

    fun process() {

        if (!this.isActive()) return

        if (this.actor.currentHitpoints() <= 0) {
            println("Praying player is dead!?!??@#")
            this.turnOff()
            return
        }

        if (this.actor is Player && this.getCurrentPrayerPoints() == 0) {
            actor.message { "You have run out of prayer points, you can recharge at an altar." }
            this.turnOff()
            return
        }

        if (actor is Player) {
            actor.message { "Ticking prayer: ${this.activeDrainRate} ${this.activePrayers}" }
        }

        if (this.activeDrainRate == 0) return

        this.drainCounter += this.activeDrainRate
        val resistance = 60 + (this.actor.bonuses.prayer * 2)

        if (this.drainCounter > resistance) {
            val spentPoints = floor(this.drainCounter.toDouble() / resistance).toInt()
            if (this.actor is Player) {
                val currentPoints = this.actor.skills.level(Skill.PRAYER)
                val newPoints = currentPoints - spentPoints

                // set prayer points
                this.actor.setLevel(Skill.PRAYER, newPoints)
                // update drainCounter
                this.drainCounter -= resistance * spentPoints
            }
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

            // TODO dragon scim special attack disables overheads for 5 seconds

            if (this.getCurrentPrayerPoints() <= 0) return

            // turn off conflicting prayers
            PrayerType.getConflictingTypes(prayerInfo.prayerType)?.let { this.deactivate(*it) }

            // update activePrayerMap
            activePrayers++
            this.activePrayerMap[prayerInfo.prayerType] = prayerInfo
            Timer().schedule(
                timerTask {
                    activeDrainRate += prayerInfo.drainRate
                    println("after time $activeDrainRate")
                },
                1200
            )

            // TODO turn on the prayer's varbit
//            if (this.player is Player) this.player.vars[prayerInfo.varbit] = 1

            // if the prayer has an overhead icon turn it on
            prayerInfo.icon?.let { this.actor.prayerIcon { it } }
        }

        // debugging
        println(activePrayerMap.values.mapNotNull { it?.name })
    }

    fun activate(prayer: Prayers) {
        if (!this.isActive(prayer)) this.switch(prayer)
    }

    fun deactivate(prayer: Prayers) {
        // get the prayer's info and if the prayer is active deactivate it
        Prayers.info(prayer.prayerName)?.let { if (this.isActive(it)) this.deactivate(it.prayerType) }
    }

    fun deactivate(vararg prayerTypes: PrayerType) {
        // for each of the PrayerTypes
        prayerTypes.forEach {
            // get active prayer by PrayerType. continue if no prayer of that PrayerType is active
            val current = this.activePrayerMap[it] ?: return@forEach

            // set the prayer null "off" in the activePrayerMap
            this.activePrayers--
            this.activeDrainRate -= current.drainRate
            this.activePrayerMap[it] = null

            println("deactivating $current ${this.activeDrainRate}")

            // turn off the prayer icon if the prayer has one
            current.icon?.let { this.actor.prayerIcon { PrayerIconType.NONE } }

            // check if actor is a Player to access var system
            if (this.actor is Player) {
                // TODO turn off the prayer's varbit
                this.actor.vars[current.varbit] = 0

                // TODO if no prayers are active and the quick prayer varbit is on turn it off
                if (!this.isActive() && this.actor.vars[4103] == 1) this.actor.vars[4103] = 0
            }
        }
    }

    fun turnOff() {
        println("Turning prayers off")
        this.deactivate(*PrayerType.values())
        if (this.actor is Player && this.actor.vars[4103] == 1) {
            this.actor.vars[4103] = 0
            println("Turned off the quick prayers varbit")
        }
    }

    fun reset() {
        // hard reset of the prayer system for the player

        this.activePrayers = 0
        this.drainCounter = 0
        this.activeDrainRate = 0
        this.rechargePrayerPoints()
        this.activePrayerMap.forEach { this.activePrayerMap[it.key] = null }
        // TODO turn off all prayer varbits         Prayers.info().values.foreach
        // TODO turn off quick prayer varbit
    }

    fun rechargePrayerPoints(): Int {
        if (this.actor is Player) {
            val points = this.getCurrentPrayerPoints()
            if (points >= this.getMaxPrayerPoints()) {
                this.actor.message { "You already have full Prayer points." }
                return 0
            }
            return this.actor.setLevelToBase(Skill.PRAYER) - points
        }
        return 0 // npc prayer points a thing?
    }

    fun prayAtAltar(gameObject: GameObject? = null) {
        gameObject?.let { this.actor.angleTo(it) }

        // TODO animations and sounds

        this.rechargePrayerPoints()
    }

    fun isActive(): Boolean = this.activePrayers > 0

    fun isActive(prayer: Prayers): Boolean {
        return Prayers.info(prayer.prayerName)?.let { this.isActive(it) } ?: false
    }

    private fun isActive(prayerInfo: PrayerInfoResource): Boolean {
        return activePrayerMap[prayerInfo.prayerType]?.name == prayerInfo.name
    }

    fun getDrainRate(): Int = this.activeDrainRate

    fun isFull(): Boolean = if (this.actor is Player) this.getCurrentPrayerPoints() >= this.getMaxPrayerPoints() else true
    // TODO is prayer empty?

    fun getMaxPrayerPoints(): Int = if (this.actor is Player) this.actor.getBaseLevel(Skill.PRAYER) else 99

    fun getCurrentPrayerPoints(): Int = if (this.actor is Player) this.actor.skills.level(Skill.PRAYER) else 99 // npc prayer points a thing?

    // TODO set prayer points (canBoost: Boolean)
    // TODO get missing prayer points
    // TODO isBoosted?, isDrained?
    // TODO boostPrayerPoints
    // TODO reducePrayerPoints

//    fun setPrayerPoints(points: Int, canBoost: Boolean) {
//        if (this.actor is Player) {
//            if (points > this.getMaxPrayerPoints()) {
//                if (canBoost) points else this.getMaxPrayerPoints()
//            }
//            this.actor.skills.setLevel(Skill.PRAYER, points)
//        }
//    }

    fun getActivePrayers(): Map<PrayerType, PrayerInfoResource?> = this.activePrayerMap.filter { it.value != null }.toMap()
}

fun Actor.prayerIcon(prayerIcon: () -> PrayerIconType) {
    if (this is Player) {
        this.appearance.headIcon = Optional.of(prayerIcon.invoke().overheadId)
        this.renderAppearance()
    } else {
        // npc overhead icons
    }
}

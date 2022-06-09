package xlitekt.game.actor.prayer

import xlitekt.game.actor.Actor
import xlitekt.game.actor.angleTo
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message
import xlitekt.game.actor.player.setLevel
import xlitekt.game.actor.player.setLevelToNormal
import xlitekt.game.actor.prayerIcon
import xlitekt.game.content.skill.Skill
import xlitekt.game.world.map.GameObject
import xlitekt.shared.resource.prayer.PrayerIconType
import xlitekt.shared.resource.prayer.PrayerInfoResource
import xlitekt.shared.resource.prayer.PrayerType
import xlitekt.shared.resource.prayer.Prayers
import kotlin.math.floor

/**
 * @author Justin Kenney
 */

class Prayer(
    private val player: Actor
) {
    private val activePrayerMap: MutableMap<PrayerType, PrayerInfoResource?> = PrayerType.values().associateWith { null }.toMutableMap()

    private var activePrayers = 0
    private var activeDrainRate = 0
    private var drainCounter = 0

    fun process() {

        if (!this.isActive()) return

        if (this.player.currentHitpoints() <= 0) {
            println("Praying player is dead!?!??@#")
            this.turnOff()
            return
        }

        val drain = this.getDrainRate().also { if (it == 0) return }

        // TODO if player Prayer Level = 0 or player Prayer Points = 0
        if (this.player is Player && this.player.skills.level(Skill.PRAYER) == 0) {
            player.message { "You have run out of prayer points, you can recharge at an altar." }
            this.turnOff()
            return
        }

        this.drainCounter += drain
        val resistance = 60 + (this.player.bonuses.prayer * 2)

        if (this.drainCounter > resistance) {
            val spentPoints = floor(this.drainCounter.toDouble() / resistance).toInt()
            if (this.player is Player) {
                val currentPoints = this.player.skills.level(Skill.PRAYER)
                val newPoints = currentPoints - spentPoints

                // set prayer points
                this.player.setLevel(Skill.PRAYER, newPoints)
                // update drainCounter
                this.drainCounter -= resistance * spentPoints
            }
        }

        if (player is Player) {
            player.message { "Ticking prayer: $drain ${this.activePrayers}" }
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

            if (this.getCurrentPrayerPoints() <= 0) return

            // turn off conflicting prayers
            PrayerType.getConflictingTypes(prayerInfo.prayerType)?.let { this.deactivate(*it) }

            // update activePrayerMap
            this.activePrayers++
            this.activeDrainRate += prayerInfo.drainRate
            this.activePrayerMap[prayerInfo.prayerType] = prayerInfo

            // TODO turn on the prayer's varbit
//            if (this.player is Player) this.player.vars[prayerInfo.varbit] = 1

            // if the prayer has an overhead icon turn it on
            prayerInfo.icon?.let { this.player.prayerIcon { it } }
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

            println("deactivating $current")

            // turn off the prayer icon if the prayer has one
            current.icon?.let { this.player.prayerIcon { PrayerIconType.NONE } }

            // check if actor is a Player to access var system
            if (this.player is Player) {
                // TODO turn off the prayer's varbit
                this.player.vars[current.varbit] = 0

                // TODO if no prayers are active and the quick prayer varbit is on turn it off
                if (!this.isActive() && this.player.vars[4103] == 1) this.player.vars[4103] = 0
            }
        }
    }

    fun turnOff() {
        println("Turning prayers off")
        this.deactivate(*PrayerType.values())
        if (this.player is Player && this.player.vars[4103] == 1) {
            this.player.vars[4103] = 0
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
        if (this.player is Player) {
            val points = this.getCurrentPrayerPoints()
            if (points >= Skill.getLevelForXp(this.player.skills.xp(Skill.PRAYER))) {
                this.player.message { "You already have full Prayer points." }
                return points
            }
            return this.player.setLevelToNormal(Skill.PRAYER)
        }
        return 99 // npc prayer points a thing?
    }

    fun prayAtAltar(gameObject: GameObject? = null) {
        gameObject?.let { this.player.angleTo(it) }

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

    fun isFull(): Boolean = if (this.player is Player) this.getCurrentPrayerPoints() >= Skill.getLevelForXp(this.player.skills.xp(Skill.PRAYER)) else true

    fun getCurrentPrayerPoints(): Int = if (this.player is Player) this.player.skills.level(Skill.PRAYER) else 99 // npc prayer points a thing?

    fun getActivePrayers(): Map<PrayerType, PrayerInfoResource?> = this.activePrayerMap.filter { it.value != null }.toMap()
}

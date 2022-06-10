package xlitekt.game.content.skill

import kotlinx.serialization.Serializable
import xlitekt.game.actor.player.*
import xlitekt.game.actor.player.serializer.SkillsSerializer
import xlitekt.game.content.skill.Skill.Companion.MAX_EXPERIENCE
import xlitekt.game.content.skill.Skill.Companion.MAX_LEVEL
import xlitekt.game.content.skill.Skill.Companion.MAX_SKILLS
import xlitekt.game.content.skill.Skill.Companion.getLevelForXp
import xlitekt.game.content.skill.Skill.Companion.getXpForLevel
import xlitekt.game.packet.UpdateStatPacket

@Serializable(SkillsSerializer::class)
class Skills(
    private val levels: IntArray = IntArray(MAX_SKILLS),
    private val experience: DoubleArray = DoubleArray(MAX_SKILLS)
) {
//    init {
//        val defaultExperience = getXPForLevel(DEFAULT_LEVEL)
//
//        Skill.values().forEach {
//            val (level, experience) = when (it) {
//                Skill.HITPOINTS -> DEFAULT_HITPOINTS_LEVEL to getXPForLevel(DEFAULT_HITPOINTS_LEVEL)
//                Skill.HERBLORE -> DEFAULT_HERBLORE_LEVEL to getXPForLevel(DEFAULT_HERBLORE_LEVEL)
//                else -> DEFAULT_LEVEL to defaultExperience
//            }
//            this.levels[it.id] = level
//            this.experience[it.id] = experience
//        }
//    }

    fun level(skill: Skill): Int = levels[skill.id]
    fun xp(skill: Skill): Double = experience[skill.id]

    internal fun setLevel(skill: Skill, level: Int) = this.levels.set(skill.id, level)
    internal fun setExperience(skill: Skill, experience: Double) = this.experience.set(skill.id, experience)

    fun isMaxLevel(skill: Skill): Boolean = this.levels[skill.id] == MAX_LEVEL
    fun isMaxExperience(skill: Skill): Boolean = this.experience[skill.id] == MAX_EXPERIENCE
}

fun Player.updateStat(skill: Skill) = this.updateStat(skill, this.skills.level(skill), this.skills.xp(skill))

fun Player.updateStat(skill: Skill, level: Int, experience: Double) {
    write(UpdateStatPacket(skill.id, level, experience))
}

fun Player.addExperience(skill: Skill, experience: Double): Double {
    val newExperience = this.skills.xp(skill) + experience
    this.setLevelByExperience(skill, newExperience)

    return newExperience
}

fun Player.setLevel(skill: Skill, level: Int) {
    val newLevel = level.coerceIn(0, MAX_LEVEL)
    this.skills.setLevel(skill, newLevel)

    this.updateStat(skill)
}

fun Player.getBaseLevel(skill: Skill): Int = getLevelForXp(this.skills.xp(skill))

// not sure what to call this
fun Player.isLevelNormal(skill: Skill): Boolean = getLevelForXp(this.skills.xp(skill)) == this.skills.level(skill)

fun Player.setLevelToBase(skill: Skill): Int {
    val level = getLevelForXp(this.skills.xp(skill))
    this.skills.setLevel(skill, level)

    this.updateStat(skill)
    return level
}

fun Player.setExperienceByLevel(skill: Skill, level: Int): Double {
    val newLevel = level.coerceIn(0, MAX_LEVEL)
    this.skills.setLevel(skill, newLevel)

    val experience = getXpForLevel(newLevel)
    this.skills.setExperience(skill, experience)

    this.updateStat(skill)
    return experience
}

fun Player.setLevelByExperience(skill: Skill, experience: Double): Int {
    val newExperience = experience.coerceIn(0.0, MAX_EXPERIENCE)
    if (newExperience == MAX_EXPERIENCE && this.skills.isMaxExperience(skill)) return MAX_LEVEL
    this.skills.setExperience(skill, newExperience)

    val level = getLevelForXp(newExperience)
    this.skills.setLevel(skill, level)

    this.updateStat(skill)
    return level
}

package xlitekt.game.content.skill

import kotlinx.serialization.Serializable
import xlitekt.game.actor.player.*
import xlitekt.game.actor.player.serializer.SkillsSerializer
import xlitekt.game.content.skill.Skill.Companion.MAX_SKILLS
import xlitekt.game.content.skill.Skill.Companion.getLevelForXp
import xlitekt.game.content.skill.Skill.Companion.getXPForLevel

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

    fun setLevel(skill: Skill, level: Int, player: Player) {
        val newLevel = level.coerceIn(0, 99)
        this.levels[skill.id] = newLevel

        this.updateStat(skill, player)
    }

    fun setLevelToNormal(skill: Skill, player: Player): Int {
        val level = getLevelForXp(this.experience[skill.id])
        this.levels[skill.id] = level

        this.updateStat(skill, player)
        return level
    }

    fun setExperience(skill: Skill, experience: Double, player: Player) {
        val newExperience = experience.coerceIn(0.0, 200000000.0)
        this.experience[skill.id] = newExperience

        this.updateStat(skill, player)
    }

    fun setExperienceByLevel(skill: Skill, level: Int, player: Player): Double {
        val newLevel = level.coerceIn(0, 99)
        this.levels[skill.id] = newLevel

        val experience = getXPForLevel(newLevel)
        this.experience[skill.id] = experience

        this.updateStat(skill, player)
        return experience
    }

    fun setLevelByExperience(skill: Skill, experience: Double, player: Player): Int {
        val newExperience = experience.coerceIn(0.0, 200000000.0)
        this.experience[skill.id] = newExperience

        val level = getLevelForXp(newExperience)
        this.levels[skill.id] = level

        this.updateStat(skill, player)
        return level
    }

    private fun updateStat(skill: Skill, player: Player) {
        player.updateStat(skill, this.levels[skill.id], this.experience[skill.id])
    }
}

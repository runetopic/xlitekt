package xlitekt.game.content.skill

import kotlinx.serialization.Serializable
import xlitekt.game.actor.player.serializer.SkillsSerializer
import xlitekt.game.content.skill.Skill.Companion.MAX_SKILLS
import xlitekt.game.content.skill.Skill.Companion.getLevelForXp

@Serializable(SkillsSerializer::class)
class Skills(
    private val levels: IntArray = IntArray(MAX_SKILLS),
    private val experience: DoubleArray = DoubleArray(MAX_SKILLS)
) {
//    init {
//        val defaultExperience = getXpForLevel(DEFAULT_LEVEL)
//
//        Skill.values().forEach {
//            val (level, experience) = when (it) {
//                Skill.HITPOINTS -> DEFAULT_HITPOINTS_LEVEL to getXpForLevel(DEFAULT_HITPOINTS_LEVEL)
//                Skill.HERBLORE -> DEFAULT_HERBLORE_LEVEL to getXpForLevel(DEFAULT_HERBLORE_LEVEL)
//                else -> DEFAULT_LEVEL to defaultExperience
//            }
//            this.levels[it.id] = level
//            this.experience[it.id] = experience
//        }
//    }

    fun level(skill: Skill): Int = levels[skill.id]
    fun xp(skill: Skill): Double = experience[skill.id]

    internal fun addExperience(skill: Skill, experience: Double, function: (level: Int, experience: Double) -> Unit) {
        val newExperience = this.xp(skill) + experience
        val level = getLevelForXp(newExperience)

        this.experience[skill.id] = newExperience

        if (this.levels[skill.id] != level)
            this.levels[skill.id] = level

        function.invoke(level, newExperience)
    }
}


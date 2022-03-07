package xlitekt.game.actor.skill

import kotlinx.serialization.Serializable
import xlitekt.game.actor.player.serializer.SkillsSerializer
import xlitekt.game.actor.skill.Skill.Companion.DEFAULT_HERBLORE_LEVEL
import xlitekt.game.actor.skill.Skill.Companion.DEFAULT_HITPOINTS_LEVEL
import xlitekt.game.actor.skill.Skill.Companion.DEFAULT_LEVEL
import xlitekt.game.actor.skill.Skill.Companion.MAX_SKILLS
import xlitekt.game.actor.skill.Skill.Companion.getXPForLevel

@Serializable(SkillsSerializer::class)
class Skills(
    val levels: IntArray = IntArray(MAX_SKILLS),
    val experience: DoubleArray = DoubleArray(MAX_SKILLS)
) {
    init {
        val defaultExperience = getXPForLevel(DEFAULT_LEVEL)

        Skill.values().forEach {
            val (level, experience) = when (it) {
                Skill.HITPOINTS -> DEFAULT_HITPOINTS_LEVEL to getXPForLevel(DEFAULT_HITPOINTS_LEVEL)
                Skill.HERBLORE -> DEFAULT_HERBLORE_LEVEL to getXPForLevel(DEFAULT_HERBLORE_LEVEL)
                else -> DEFAULT_LEVEL to defaultExperience
            }
            this.levels[it.id] = level
            this.experience[it.id] = experience
        }
    }
}

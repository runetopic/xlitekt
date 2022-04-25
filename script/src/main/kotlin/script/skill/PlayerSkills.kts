package script.skill

import xlitekt.game.actor.player.updateStat
import xlitekt.game.content.skill.Skill
import xlitekt.game.event.impl.Events
import xlitekt.game.event.onEvent

onEvent<Events.OnLoginEvent> {
    Skill.values().forEach {
        val level = player.skills.levels[it.id]
        val experience = player.skills.experience[it.id]
        player.updateStat(it, level, experience)
    }
}

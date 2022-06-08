package script.skill

import xlitekt.game.actor.player.updateStat
import xlitekt.game.content.skill.Skill
import xlitekt.game.event.impl.Events
import xlitekt.game.event.onEvent

onEvent<Events.OnLoginEvent> {
    Skill.values().forEach {
        val level = player.skills.level(it)
        val experience = player.skills.xp(it)
        player.updateStat(it, level, experience)
    }
}

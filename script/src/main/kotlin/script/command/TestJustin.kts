package script.command

import xlitekt.game.content.command.Commands
import xlitekt.game.content.skill.Skill
import xlitekt.game.content.skill.setLevelByExperience
import xlitekt.shared.resource.prayer.Prayers

Commands.onCommand("resetprayer").use {
    this.prayer.turnOff()
}

Commands.onCommand("pray").use {
    this.prayer.activate(Prayers.RAPID_HEAL)
    this.prayer.activate(Prayers.RAPID_RESTORE)
    this.prayer.activate(Prayers.PROTECT_ITEM)
    this.prayer.activate(Prayers.PROTECT_FROM_MAGIC)
    this.prayer.activate(Prayers.INCREDIBLE_REFLEXES)
    this.prayer.activate(Prayers.STEEL_SKIN)
    this.prayer.activate(Prayers.ULTIMATE_STRENGTH)
}

Commands.onCommand("max").use {
    Skill.values().forEach {
//        this.setLevelAndExperience(it, 78)
        this.setLevelByExperience(it, 10000000.0)
    }
}

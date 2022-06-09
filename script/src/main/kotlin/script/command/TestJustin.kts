package script.command

import xlitekt.game.actor.activatePrayer
import xlitekt.game.actor.player.setLevelByExperience
import xlitekt.game.content.command.Commands
import xlitekt.game.content.skill.Skill
import xlitekt.shared.resource.prayer.Prayers

Commands.onCommand("resetprayer").use {
    this.prayer.turnOff()
}

Commands.onCommand("pray").use {
    this.activatePrayer { Prayers.RAPID_HEAL }
    this.activatePrayer { Prayers.RAPID_RESTORE }
    this.activatePrayer { Prayers.PROTECT_ITEM }
    this.activatePrayer { Prayers.PROTECT_FROM_MAGIC }
    this.activatePrayer { Prayers.INCREDIBLE_REFLEXES }
    this.activatePrayer { Prayers.STEEL_SKIN }
    this.activatePrayer { Prayers.ULTIMATE_STRENGTH }
}

Commands.onCommand("max").use {
    Skill.values().forEach {
//        this.setLevelAndExperience(it, 78)
        this.setLevelByExperience(it, 50000.0)
    }
}

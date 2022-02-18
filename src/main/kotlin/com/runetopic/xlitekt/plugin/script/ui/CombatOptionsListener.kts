package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.actor.player.message
import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface
import com.runetopic.xlitekt.game.vars.VarPlayer
import com.runetopic.xlitekt.game.vars.setVar
import com.runetopic.xlitekt.game.vars.varValue

onInterface<UserInterface.CombatOptions> {
    onInit {
        setVar(VarPlayer.SpecialAttackEnergy, 100 * 10)
        message("Special attack energy ${varValue(VarPlayer.SpecialAttackEnergy)}")
    }
}

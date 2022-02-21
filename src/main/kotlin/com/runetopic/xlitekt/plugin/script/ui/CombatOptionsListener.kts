package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface
import com.runetopic.xlitekt.game.vars.VarPlayer

onInterface<UserInterface.CombatOptions> {
    onCreate {
        vars[VarPlayer.SpecialAttackEnergy] = 100 * 10
    }
}

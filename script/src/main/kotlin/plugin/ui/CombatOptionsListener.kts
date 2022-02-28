package plugin.ui

import xlitekt.game.ui.UserInterface
import xlitekt.game.ui.onInterface
import xlitekt.game.vars.VarPlayer

onInterface<UserInterface.CombatOptions> {
    onCreate {
        vars[VarPlayer.SpecialAttackEnergy] = 100 * 10
    }
}

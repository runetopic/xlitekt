package script.ui

import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

onInterface<UserInterface.CombatOptions> {
    onCreate {
        vars[VarPlayer.SpecialAttackEnergy] = 100 * 10
    }
}

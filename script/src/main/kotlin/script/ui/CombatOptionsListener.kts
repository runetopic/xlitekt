package script.ui

import xlitekt.game.content.ui.UserInterface.CombatOptions
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

/**
 * @author Tyler Telis
 */
onInterface<CombatOptions> {
    onCreate {
        vars[VarPlayer.SpecialAttackEnergy] = 100 * 10
    }
}

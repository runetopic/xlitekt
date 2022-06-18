package script.ui

import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface.CombatOptions
import xlitekt.game.content.vars.VarPlayer
import xlitekt.shared.insert

/**
 * @author Tyler Telis
 */
insert<InterfaceListener>().userInterface<CombatOptions> {
    onCreate {
        vars[VarPlayer.SpecialAttackEnergy] = 100 * 10
    }
}

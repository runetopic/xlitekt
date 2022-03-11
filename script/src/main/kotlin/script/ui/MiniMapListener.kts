package script.ui

import xlitekt.game.ui.UserInterface
import xlitekt.game.ui.onInterface
import xlitekt.game.vars.VarPlayer

onInterface<UserInterface.MiniMap> {
    onClick("Toggle Run") {
        vars.flip(VarPlayer.ToggleRun)
        movement.toggleRun()
    }
}

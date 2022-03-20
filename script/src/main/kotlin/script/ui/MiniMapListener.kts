package script.ui

import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

onInterface<UserInterface.MiniMap> {
    onClick("Toggle Run") {
        vars.flip(VarPlayer.ToggleRun)
        movement.toggleRun()
    }
}

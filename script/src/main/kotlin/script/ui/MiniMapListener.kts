package script.ui

import xlitekt.game.content.ui.UserInterface.MiniMap
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

onInterface<MiniMap> {
    onClick("Toggle Run") {
        vars.flip(VarPlayer.ToggleRun)
        movement.toggleRun()
    }
}

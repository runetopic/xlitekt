package script.ui

import xlitekt.game.actor.speed
import xlitekt.game.content.ui.UserInterface.MiniMap
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

/**
 * @author Tyler Telis
 */
onInterface<MiniMap> {
    onClick("Toggle Run") {
        vars.flip { VarPlayer.ToggleRun }
        speed { VarPlayer.ToggleRun in vars }
    }
}

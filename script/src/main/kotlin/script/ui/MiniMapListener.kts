package script.ui

import xlitekt.game.actor.player.message
import xlitekt.game.actor.speed
import xlitekt.game.content.ui.UserInterface.MiniMap
import xlitekt.game.content.ui.onInterface
import xlitekt.game.content.vars.VarPlayer

/**
 * @author Tyler Telis
 */
onInterface<MiniMap> {
    onClick("Toggle Run") {
        if (runEnergy <= 100f) {
            // TODO get the game messages for this
            message { "You don't have enough energy left to run!" }
            vars[VarPlayer.ToggleRun] = 0
            return@onClick
        }
        vars.flip { VarPlayer.ToggleRun }
        speed { VarPlayer.ToggleRun in vars }
    }
}

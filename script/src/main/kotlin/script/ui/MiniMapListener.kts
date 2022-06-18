package script.ui

import xlitekt.game.actor.player.message
import xlitekt.game.actor.speed
import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface.MiniMap
import xlitekt.game.content.vars.VarPlayer
import xlitekt.shared.insert

/**
 * @author Tyler Telis
 */
insert<InterfaceListener>().userInterface<MiniMap> {
    onClick("Toggle Run") {
        if (runEnergy < 100f) {
            // TODO get the game messages for this
            message { "You don't have enough energy left to run!" }
            vars[VarPlayer.ToggleRun] = 0
            return@onClick
        }
        vars.flip { VarPlayer.ToggleRun }
        speed { VarPlayer.ToggleRun in vars }
    }
}

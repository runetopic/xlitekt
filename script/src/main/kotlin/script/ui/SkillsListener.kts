import xlitekt.game.actor.player.message
import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface
import xlitekt.shared.insert

/**
 * @author Justin Kenney
 */
insert<InterfaceListener>().userInterface<UserInterface.Skills> {
    onClick(childId = 1) {
        message { "Clicked on attack" }
    }
}

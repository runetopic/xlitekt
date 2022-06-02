import xlitekt.game.actor.player.message
import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface

/**
 * @author Justin Kenney
 */
onInterface<UserInterface.Skills> {
    onClick(childId = 1) {
        message { "Clicked on attack" }
    }
}

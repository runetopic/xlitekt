package script.ui

import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface

/**
 * @author Jordan Abraham
 */
onInterface<UserInterface.Logout> {
    onClick("Logout") {
        logout()
    }
}

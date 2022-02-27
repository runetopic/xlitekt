package xlitekt.plugin.ui

import xlitekt.game.ui.UserInterface
import xlitekt.game.ui.onInterface

/**
 * @author Jordan Abraham
 */
onInterface<UserInterface.Logout> {
    onClick("Logout") {
        logout()
    }
}

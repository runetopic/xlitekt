package script.ui

import xlitekt.game.content.ui.InterfaceListener
import xlitekt.game.content.ui.UserInterface.Logout
import xlitekt.game.world.World
import xlitekt.shared.inject
import xlitekt.shared.insert

/**
 * @author Jordan Abraham
 */
val world by inject<World>()

insert<InterfaceListener>().userInterface<Logout> {
    onClick("Logout") {
        world.requestLogout(this)
    }
}

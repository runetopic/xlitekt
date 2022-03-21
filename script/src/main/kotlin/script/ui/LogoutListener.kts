package script.ui

import xlitekt.game.content.ui.UserInterface.Logout
import xlitekt.game.content.ui.onInterface
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
val world by inject<World>()

onInterface<Logout> {
    onClick("Logout") {
        world.requestLogout(this)
    }
}

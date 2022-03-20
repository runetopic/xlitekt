package script.ui

import xlitekt.game.content.ui.UserInterface
import xlitekt.game.content.ui.onInterface
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
val world by inject<World>()

onInterface<UserInterface.Logout> {
    onClick("Logout") {
        world.requestLogout(this)
    }
}

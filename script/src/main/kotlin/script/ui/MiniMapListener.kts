package script.ui

import xlitekt.game.ui.UserInterface
import xlitekt.game.ui.onInterface

onInterface<UserInterface.MiniMap> {
    onClick("Toggle Run") { movement.toggleRun() }
}

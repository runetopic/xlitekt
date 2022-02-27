package com.runetopic.xlitekt.plugin.script.ui

import com.runetopic.xlitekt.game.ui.UserInterface
import com.runetopic.xlitekt.game.ui.onInterface

/**
 * @author Jordan Abraham
 */
onInterface<UserInterface.Logout> {
    onClick("Logout") {
        logout()
    }
}

package script.command

import xlitekt.game.actor.player.Viewport.Companion.PREFERRED_VIEW_DISTANCE
import xlitekt.game.actor.player.message
import xlitekt.game.content.command.CommandListener
import xlitekt.shared.insert

private val invalidSyntaxMessage = "Please use syntax: ::viewport static or ::viewport dynamic"

insert<CommandListener>().command("viewport").use { arguments ->
    if (arguments.isEmpty()) {
        message { invalidSyntaxMessage }
        return@use
    }

    try {
        val command = arguments.firstOrNull() ?: run {
            message { invalidSyntaxMessage }
            return@use
        }

        when (command) {
            "static" -> {
                viewport.forceViewDistance = true
                viewport.viewDistance = PREFERRED_VIEW_DISTANCE
            }
            "dynamic" -> viewport.forceViewDistance = false
            else -> message { invalidSyntaxMessage }
        }
    } catch (exception: NumberFormatException) {
        message { invalidSyntaxMessage }
    }
}

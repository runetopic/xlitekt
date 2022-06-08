package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.actor.teleportTo
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.world.map.Location

private val teleportDescription = "Teleports the Player to the specified coordinates."
private val teleportSyntax = setOf(
    "::tele x z",
    "::tele x z level"
)

// TODO make a color system so we're not using arbitrary hex codes throughout our app
private val invalidSyntaxMessage = "Invalid syntax - Please use: ${teleportSyntax.joinToString(" or ") { "<col=FF0000>$it</col>" }}."

onCommand("tele", description = teleportDescription, syntax = teleportSyntax).use { arguments ->
    if (arguments.isEmpty()) {
        message { invalidSyntaxMessage }
        return@use
    }

    try {
        val x = arguments.firstOrNull()?.toInt() ?: run {
            message { "You must provide the x location to teleport to." }
            message { invalidSyntaxMessage }
            return@use
        }

        val z = arguments.drop(1).firstOrNull()?.toInt() ?: run {
            message { "You must provide the z location to teleport to." }
            message { invalidSyntaxMessage }
            return@use
        }

        val level = arguments.drop(2).firstOrNull()?.toInt() ?: location.level
        val destination = Location(x, z, level)

        teleportTo { destination }
        message { "Teleported: $destination" }
    } catch (exception: NumberFormatException) {
        message { invalidSyntaxMessage }
    }
}

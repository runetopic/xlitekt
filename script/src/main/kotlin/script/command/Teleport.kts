import xlitekt.game.actor.player.message
import xlitekt.game.command.Commands.onCommand
import xlitekt.game.world.map.location.Location

// TODO make a color system so we're not using arbitrary hex codes throughout our app
private val invalidSyntaxMessage = "Please use syntax: <col=FF0000>::tele x, z, level (Level is optional).</col>"

onCommand("tele").use {
    if (it.isEmpty()) {
        message(invalidSyntaxMessage)
        return@use
    }

    try {
        val x = it.firstOrNull()?.toInt() ?: run {
            message("You must provide the x location to teleport to.")
            message(invalidSyntaxMessage)
            return@use
        }

        val z = it.drop(1).firstOrNull()?.toInt() ?: run {
            message("You must provide the z location to teleport to.")
            message(invalidSyntaxMessage)
            return@use
        }

        val level = it.drop(2).firstOrNull()?.toInt() ?: location.level

        location = Location(x, z, level)
        message("Normal ${Location(x, z, level)}")
    } catch (exception: NumberFormatException) {
        message(invalidSyntaxMessage)
    }
}

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.world.map.location.Location

// TODO make a color system so we're not using arbitrary hex codes throughout our app
private val invalidSyntaxMessage = "Please use syntax: <col=FF0000>::tele x, z, level (Level is optional).</col>"

onCommand("tele").use { arguments ->
    if (arguments.isEmpty()) {
        message(invalidSyntaxMessage)
        return@use
    }

    try {
        val x = arguments.firstOrNull()?.toInt() ?: run {
            message("You must provide the x location to teleport to.")
            message(invalidSyntaxMessage)
            return@use
        }

        val z = arguments.drop(1).firstOrNull()?.toInt() ?: run {
            message("You must provide the z location to teleport to.")
            message(invalidSyntaxMessage)
            return@use
        }

        val level = arguments.drop(2).firstOrNull()?.toInt() ?: location.level

        route(location = Location(x, z, level), teleport = true)
        message("Teleported: ${Location(x, z, level)}")
    } catch (exception: NumberFormatException) {
        message(invalidSyntaxMessage)
    }
}

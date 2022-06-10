import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands

/**
 * @author Justin Kenney
 */

val helpDescription = "Displays the help menu or information for the specified command."
val helpSyntax = setOf<String>(
    "::help",
    "::help (command_name)"
)

Commands.onCommand("help", description = helpDescription, syntax = helpSyntax).use { arguments ->

    if (arguments.isEmpty()) {
        // Display menu/interface of quick help options?
        // like un-stuck (home tele thing), FAQs, Server info (website/discord), other options?
        message { "The help command is under construction." }
        return@use
    }

    val command = arguments.first().lowercase()
    val helpCommand = Commands.getCommandSet().find { it.command.contains(command) } ?: run {
        message { "$command is not a recognized command." }
        return@use
    }

    message { helpCommand.description.ifEmpty { "No description for $command" } }

    if (helpCommand.syntax.size > 1 || helpCommand.syntax.first().trim().contains(" ")) {
        val syntax = helpCommand.syntax.joinToString(" or ") { "<col=FF0000>$it</col>" }
        message { "Valid syntax for $command: $syntax." }
    }
}

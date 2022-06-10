package xlitekt.game.content.command

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message

/**
 * Tyler Telis
 */
object Commands {
    private val commandSet = mutableSetOf<CommandListener>()

    fun execute(input: String, player: Player): Boolean {
        val command = input.split(" ")

        if (command.isEmpty()) return false

        commandSet.filter { it.command.contains(command.first()) }.forEach { listener ->
            if (!listener.filter(player)) return@forEach
            listener.use(player, command.drop(1))
            return true
        }

        if (player.rights >= 2) {
            player.message { "Unhandled client cheat command: $input" }
        }
        return false
    }

    fun onCommand(vararg commands: String, description: String = "", syntax: Set<String>? = null): CommandListener {
        val listener = CommandListener(commands, description, syntax ?: setOf("::${commands.first()}"))
        this.commandSet += listener
        return listener
    }

    fun getCommandSet(): Set<CommandListener> {
        return commandSet.toSet()
    }
}

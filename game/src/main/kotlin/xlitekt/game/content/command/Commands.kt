package xlitekt.game.content.command

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message

/**
 * Tyler Telis
 */
object Commands {
    private val commands = mutableSetOf<CommandListener>()

    fun execute(input: String, player: Player): Boolean {
        val command = input.split(" ")

        if (command.isEmpty()) return false

        commands.filter { it.command == command.first() }.forEach { listener ->
            if (!listener.filter(player)) return@forEach
            listener.use(player, command.drop(1))
            return true
        }

        if (player.rights >= 2) {
            player.message { "Unhandled client cheat command: $input" }
        }
        return false
    }

    fun onCommand(command: String): CommandListener {
        val listener = CommandListener(command)
        commands += listener
        return listener
    }
}

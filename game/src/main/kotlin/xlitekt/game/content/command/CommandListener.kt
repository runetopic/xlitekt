package xlitekt.game.content.command

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message

/**
 * Tyler Telis
 */
class CommandListener : MutableList<Command> by mutableListOf() {

    fun execute(input: String, player: Player): Boolean {
        val command = input.split(" ")

        if (command.isEmpty()) return false

        filter { it.command == command.first() }.forEach { listener ->
            if (!listener.filter(player)) return@forEach
            listener.use(player, command.drop(1))
            return true
        }

        if (player.rights >= 2) {
            player.message { "Unhandled client cheat command: $input" }
        }
        return false
    }

    fun command(command: String): Command {
        val listener = Command(command)
        this += listener
        return listener
    }
}

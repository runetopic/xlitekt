package xlitekt.game.command

import xlitekt.game.actor.player.Player
import xlitekt.game.actor.player.message

/**
 * Tyler Telis
 */
object Commands {
    private val commands = mutableSetOf<CommandListener>()

    operator fun get(input: String) = commands.firstOrNull { it.command == input }

    fun execute(input: String, player: Player): Boolean {
        commands.filter { it.command == input }.forEach { listener ->
            if (!listener.filter(player)) return@forEach
            listener.use(player)
            return true
        }

        if (player.rights >= 2) {
            player.message("Unhandled client cheat command: $input")
        }
        return false
    }

    fun onCommand(command: String): CommandListener {
        val listener = CommandListener(command)
        commands += listener
        return listener
    }
}

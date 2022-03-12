package xlitekt.game.command

import xlitekt.game.actor.player.Player

/**
 * Tyler Telis
 */
class CommandListener(
    val command: String,
    var filter: Player.() -> Boolean = { true },
    var use: Player.(List<String>) -> Unit = {},
) {

    fun filter(filter: Player.() -> Boolean): CommandListener {
        this.filter = filter
        return this
    }

    fun use(function: Player.(List<String>) -> Unit): CommandListener {
        this.use = function
        return this
    }
}

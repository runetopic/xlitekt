package xlitekt.game.content.command

import xlitekt.game.actor.player.Player

/**
 * Tyler Telis
 */
class Command(
    val command: String,
    var filter: Player.() -> Boolean = { true },
    var use: Player.(List<String>) -> Unit = {},
) {
    fun filter(filter: Player.() -> Boolean): Command {
        this.filter = filter
        return this
    }

    fun use(function: Player.(List<String>) -> Unit): Command {
        this.use = function
        return this
    }
}

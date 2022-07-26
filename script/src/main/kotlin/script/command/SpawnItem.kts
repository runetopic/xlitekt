package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.CommandListener
import xlitekt.game.content.item.Item
import xlitekt.shared.insert

/**
 * @author Tyler Telis
 */

// TODO make a color system so we're not using arbitrary hex codes throughout our app
private val invalidSyntaxMessage = "Please use syntax: <col=FF0000>::item item_id, amount (optional).</col>"

insert<CommandListener>().command("item").use { arguments ->
    if (arguments.isEmpty()) {
        message { invalidSyntaxMessage }
        return@use
    }

    try {
        val itemId = arguments.firstOrNull()?.toInt() ?: run {
            message { "You must provide the item id you wish to spawn." }
            message { invalidSyntaxMessage }
            return@use
        }
        val amount = arguments.drop(1).firstOrNull()?.toInt() ?: 1
        val item = Item(itemId, amount)
        inventory.addItem(item) {
            message { "Spawned x$amount ${item.entry?.name ?: item.id}." }
        }
    } catch (exception: NumberFormatException) {
        message { invalidSyntaxMessage }
    }
}

package script.command

import xlitekt.game.actor.player.message
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.item.Item

// TODO make a color system so we're not using arbitrary hex codes throughout our app
private val invalidSyntaxMessage = "Please use syntax: <col=FF0000>::item item_id, amount (optional).</col>"

onCommand("item").use { arguments ->
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
        inventory.add(Item(itemId, amount))
    } catch (exception: NumberFormatException) {
        message { invalidSyntaxMessage }
    }
}

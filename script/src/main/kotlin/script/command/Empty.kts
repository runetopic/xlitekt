package script.command

import xlitekt.game.content.command.CommandListener
import xlitekt.shared.insert

/**
 * @author Tyler Telis
 */
insert<CommandListener>().command("empty").use { inventory.empty() }

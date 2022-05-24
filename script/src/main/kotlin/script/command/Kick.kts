package script.command

import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("kickall").use {
    world.players().filter { it != this }.onEach(world::requestLogout)
}

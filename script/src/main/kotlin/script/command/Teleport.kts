import xlitekt.game.actor.player.message
import xlitekt.game.command.Commands.onCommand

onCommand("tele").filter { rights == 0 }.use {
    message("Normal")
}

onCommand("tele").filter { rights == 2 }.use {
    message("Admin")
}

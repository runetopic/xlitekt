package script.command

import xlitekt.game.content.command.Commands

Commands.onCommand("resetprayer").use {
    this.prayer.turnOff()
}

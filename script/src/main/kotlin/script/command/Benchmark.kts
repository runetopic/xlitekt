package script.command

import xlitekt.game.actor.player.Client
import xlitekt.game.actor.player.Player
import xlitekt.game.content.command.Commands.onCommand
import xlitekt.game.content.vars.VarBit
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import kotlin.random.Random

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

onCommand("benchmark").use {
    repeat(World.MAX_PLAYERS) {
        val bot = Player(username = it.toString(), password = "")
        bot.location = Location(Random.nextInt(3200, 3280), Random.nextInt(3200, 3280), 0)
        world.addPlayer(bot)
        bot.vars.flip { VarPlayer.ToggleRun }
        bot.vars.flip { VarBit.HitSplatTinting }
        world.requestLogin(bot, Client())
    }
}

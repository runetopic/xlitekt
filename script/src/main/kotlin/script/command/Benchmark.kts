package script.command

import com.runetopic.cryptography.toISAAC
import xlitekt.game.actor.player.Client
import xlitekt.game.actor.player.Player
import xlitekt.game.content.command.CommandListener
import xlitekt.game.content.vars.VarBit
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.world.World
import xlitekt.game.world.map.Location
import xlitekt.shared.inject
import xlitekt.shared.insert
import kotlin.random.Random

/**
 * @author Jordan Abraham
 */
private val world by inject<World>()

insert<CommandListener>().command("benchmark").use {
    repeat(World.MAX_PLAYERS) {
        val bot = Player(username = it.toString(), password = "", brandNew = false)
        bot.location = Location(Random.nextInt(3200, 3280), Random.nextInt(3200, 3280), 0)
        world.addPlayer(bot)
        bot.vars.flip { VarPlayer.ToggleRun }
        bot.vars.flip { VarBit.HitSplatTinting }
        val client = Client()
        client.setIsaacCiphers(intArrayOf(0, 0, 0, 0).toISAAC(), intArrayOf(0, 0, 0, 0).toISAAC())
        world.requestLogin(bot, client)
    }
}

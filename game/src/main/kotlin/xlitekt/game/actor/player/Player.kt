package xlitekt.game.actor.player

import kotlinx.serialization.Serializable
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.PlayerEncoder.encodeToJson
import xlitekt.game.actor.player.serializer.PlayerSerializer
import xlitekt.game.actor.render.Render
import xlitekt.game.packet.MessageGamePacket
import xlitekt.game.packet.Packet
import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.RunClientScriptPacket
import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.VarpLargePacket
import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.ui.Interfaces
import xlitekt.game.vars.Vars
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import kotlin.random.Random
import xlitekt.game.packet.LogoutPacket

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable(with = PlayerSerializer::class)
class Player(
    override var location: Location = Location(3222, 3222),
    val username: String,
    val password: String,
    val rights: Int = 0,
    val appearance: Render.Appearance = Render.Appearance(),
    var runEnergy: Float = 10_000f
) : Actor(location) {
    val viewport = Viewport(this)
    val interfaces = Interfaces(this)
    val vars = Vars(this)

    private var client: Client? = null

    var online = false

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    fun login(client: Client) {
        this.client = client
        previousLocation = location
        write(RebuildNormalPacket(viewport, location, true))
        updateAppearance()
        interfaces.login()
        vars.login()
        sendUpdateRunEnergy()
        // Set the player online here, so they start processing by the main game loop.
        online = true
        // eventBus.notify(Events.OnLoginEvent(this))

        if (username == "jordan") {
            repeat(2000) {
                val bot = Player(username = "", password = "")
                bot.location = Location(Random.nextInt(3210, 3260), Random.nextInt(3210, 3260), 0)
                inject<World>().value.players.add(bot)
                bot.login(Client())
            }
        }
    }

    fun logout() {
        if (!online) return
        write(LogoutPacket(0))
        flushPool()
        online = false
        inject<World>().value.players.remove(this)
        encodeToJson()
    }

    fun updateAppearance() = renderer.appearance(appearance)

    fun write(packet: Packet) = client?.writePacket(packet)
    fun flushPool() = client?.writeChannel?.flush()
}

fun Player.sendVarp(id: Int, value: Int) = if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
    write(VarpLargePacket(id, value))
} else {
    write(VarpSmallPacket(id, value))
}

fun Player.message(message: String) = write(MessageGamePacket(0, message, false)) // TODO build messaging system
fun Player.script(scriptId: Int, parameters: List<Any>) = write(RunClientScriptPacket(scriptId, parameters))
fun Player.sendUpdateRunEnergy() = write(UpdateRunEnergyPacket(runEnergy / 100))

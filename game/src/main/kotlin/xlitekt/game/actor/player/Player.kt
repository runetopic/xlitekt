package xlitekt.game.actor.player

import kotlinx.serialization.Serializable
import xlitekt.game.actor.Actor
import xlitekt.game.actor.player.PlayerEncoder.encodeToJson
import xlitekt.game.actor.player.serializer.PlayerSerializer
import xlitekt.game.actor.render.Render
import xlitekt.game.actor.skill.Skill
import xlitekt.game.actor.skill.Skills
import xlitekt.game.content.ui.Interfaces
import xlitekt.game.content.vars.VarPlayer
import xlitekt.game.content.vars.Vars
import xlitekt.game.event.EventBus
import xlitekt.game.event.impl.Events
import xlitekt.game.packet.LogoutPacket
import xlitekt.game.packet.MessageGamePacket
import xlitekt.game.packet.Packet
import xlitekt.game.packet.RebuildNormalPacket
import xlitekt.game.packet.RunClientScriptPacket
import xlitekt.game.packet.UpdateRunEnergyPacket
import xlitekt.game.packet.UpdateStatPacket
import xlitekt.game.packet.VarpLargePacket
import xlitekt.game.packet.VarpSmallPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.shared.inject
import kotlin.math.abs
import kotlin.random.Random.Default.nextInt

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
    val appearance: Render.Appearance = Render.Appearance().also { it.displayName = username },
    val skills: Skills = Skills(),
    var runEnergy: Float = 10_000f
) : Actor(location) {
    val viewport = Viewport(this)
    val interfaces = Interfaces(this)
    val vars = Vars(this)
    var lastLoadedLocation: Location? = null

    private var client: Client? = null

    var online = false

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    fun init(client: Client) {
        this.client = client
        previousLocation = location
        lastLoadedLocation = location
        sendRebuildNormal(true)
        interfaces.openTop(interfaces.currentInterfaceLayout.interfaceId)
        flushPool()
        login()
    }

    private fun login() {
        vars.login()
        interfaces.login()

        updateAppearance()
        movementType(false)
        sendUpdateRunEnergy()
        if (vars[VarPlayer.ToggleRun] == 1) movement.toggleRun()
        inject<EventBus>().value.notify(Events.OnLoginEvent(this))

        // Set the player online here, so they start processing by the main game loop.
        online = true

        if (username == "jordan") {
            repeat(1999) {
                val bot = Player(username = "", password = "")
                bot.location = Location(nextInt(3200, 3280), nextInt(3200, 3280), 0)
                val world by inject<World>()
                world.players.add(bot)
                bot.vars.flip(VarPlayer.ToggleRun)
                world.requestLogin(bot, Client())
            }
        }
    }

    fun logout() {
        if (!online) return
        online = false
        write(LogoutPacket(0))
        flushPool()
        client?.socket?.close()
        inject<World>().value.players.remove(this)
        encodeToJson()
    }

    fun updateAppearance() = renderer.appearance(appearance)

    fun write(packet: Packet) = client?.poolPacket(packet)
    fun flushPool() = client?.flushPool()
}

fun Player.sendVarp(id: Int, value: Int) = if (value < Byte.MIN_VALUE || value > Byte.MAX_VALUE) {
    write(VarpLargePacket(id, value))
} else {
    write(VarpSmallPacket(id, value))
}

fun Player.updateStat(skill: Skill, level: Int, experience: Double) {
    write(UpdateStatPacket(skill.id, level, experience))
}

fun Player.message(message: String) = write(MessageGamePacket(0, message, false)) // TODO build messaging system
fun Player.script(scriptId: Int, parameters: List<Any>) = write(RunClientScriptPacket(scriptId, parameters))
fun Player.sendUpdateRunEnergy() = write(UpdateRunEnergyPacket(runEnergy / 100))

fun Player.shouldRebuildMap(): Boolean {
    val lastZoneX = lastLoadedLocation?.zoneX ?: 0
    val lastZoneZ = lastLoadedLocation?.zoneZ ?: 0
    val zoneX = location.zoneX
    val zoneZ = location.zoneZ
    val size = ((104 shr 3) / 2) - 1
    return abs(lastZoneX - zoneX) >= size || abs(lastZoneZ - zoneZ) >= size
}

fun Player.sendRebuildNormal(update: Boolean) {
    write(RebuildNormalPacket(viewport, location, update))
    lastLoadedLocation = location
}

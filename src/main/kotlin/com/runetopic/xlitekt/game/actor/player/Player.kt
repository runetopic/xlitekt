package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.player.PlayerEncoder.encodeToJson
import com.runetopic.xlitekt.game.actor.player.serializer.PlayerSerializer
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.Events
import com.runetopic.xlitekt.game.ui.Interfaces
import com.runetopic.xlitekt.game.vars.Vars
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.game.world.map.location.Location
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.LogoutPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.network.packet.UpdateRunEnergyPacket
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.koin.inject
import com.runetopic.xlitekt.shared.buffer.writePacket
import kotlin.concurrent.fixedRateTimer
import kotlin.random.Random
import kotlinx.serialization.Serializable

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

    private val eventBus by inject<EventBus>()
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
        eventBus.notify(Events.OnLoginEvent(this))

        if (username == "jordan") {
            repeat(750) {
                val bot = Player(username = "penis munch", password = "")
                bot.location = Location(Random.nextInt(3100, 3300), Random.nextInt(3100, 3300), 0)
                inject<World>().value.players.add(bot)
                bot.login(Client())
            }
        }
    }

    fun logout() {
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

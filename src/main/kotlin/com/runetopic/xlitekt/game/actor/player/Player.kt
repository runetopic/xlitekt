package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
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
import com.runetopic.xlitekt.network.packet.VarpLargePacket
import com.runetopic.xlitekt.network.packet.VarpSmallPacket
import com.runetopic.xlitekt.plugin.koin.inject
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import java.nio.file.Files.createDirectories
import java.nio.file.Files.notExists
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
@Serializable(with = PlayerSerializer::class)
class Player(
    val username: String,
) : Actor(Location(3222, 3222)) {
    private val eventBus by inject<EventBus>()
    private var client: Client? = null

    val viewport = Viewport(this)
    val interfaces = Interfaces(this)
    val vars = Vars(this)

    var appearance = Render.Appearance(Render.Appearance.Gender.MALE, -1, -1, -1, false)

    var rights = 2
    var online = false

    override fun totalHitpoints(): Int = 100
    override fun currentHitpoints(): Int = 100

    fun login(client: Client) {
        this.client = client
        previousLocation = location
        write(RebuildNormalPacket(viewport, location, true))
        refreshAppearance()
        interfaces.login()
        vars.login()
        // Set the player online here, so they start processing by the main game loop.
        online = true
        eventBus.notify(Events.OnLoginEvent(this))
    }

    fun logout() {
        write(LogoutPacket(0))
        flushPool()
        online = false
        inject<World>().value.players.remove(this)

        Path.of("./accounts/").apply {
            if (notExists()) createDirectories()
        }.also {
            Json.encodeToStream(this, Path.of("$it/$username.json").outputStream())
        }
    }

    // TODO build appearance manager for changing gender and appearance related stuff
    fun refreshAppearance(appearance: Render.Appearance = this.appearance): Render.Appearance {
        this.appearance = renderer.appearance(appearance)
        return this.appearance
    }

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

package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.event.EventBus
import com.runetopic.xlitekt.game.event.impl.Events
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.game.ui.Interfaces
import com.runetopic.xlitekt.game.varp.Vars
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.Packet
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import com.runetopic.xlitekt.network.packet.RunClientScriptPacket
import com.runetopic.xlitekt.plugin.koin.inject

/**
 * @author Jordan Abraham
 * @author Tyler Telis
 */
class Player(
    val username: String,
) : Actor(Tile(3222, 3222)) {
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
        previousTile = tile
        write(RebuildNormalPacket(viewport, tile, true))
        refreshAppearance()
        interfaces.login()
        vars.login()
        // Set the player online here, so they start processing by the main game loop.
        online = true
        eventBus.notify(Events.OnLoginEvent(this))
    }

    fun logout() {
        online = false
        inject<World>().value.players.remove(this)
    }

    // TODO build appearance manager for changing gender and appearance related stuff
    fun refreshAppearance(appearance: Render.Appearance = this.appearance): Render.Appearance {
        this.appearance = renderer.appearance(appearance)
        return this.appearance
    }

    fun write(packet: Packet) = client?.writePacket(packet)
    fun flushPool() = client?.writeChannel?.flush()
}

fun Player.message(message: String) = write(MessageGamePacket(0, message, false)) // TODO build messaging system
fun Player.script(scriptId: Int, parameters: List<Any>) = write(RunClientScriptPacket(scriptId, parameters))

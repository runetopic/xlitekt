package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.map.Viewport
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.MessageGamePacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.RebuildNormalPacket
import kotlinx.coroutines.runBlocking
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class Player(
    val client: Client,
    var displayName: String
) : Actor(Tile(3222, 3222)) {
    var rights = 2
    var viewport = Viewport(this)

    suspend fun login() {
        this.previousTile = this.tile
        client.writePacket(RebuildNormalPacket(viewport, tile, true))
        client.writePacket(IfOpenTopPacket(161))
        renderer.appearance(Render.Appearance.Gender.MALE, -1, -1, false)

        client.writePacket(MessageGamePacket(0, "Testing messages"))
        // TODO Just for now loop it here.
        val service = Executors.newScheduledThreadPool(1)
        service.scheduleAtFixedRate({
            runBlocking {
                client.writePacket(PlayerInfoPacket(this@Player))
                renderer.clearUpdates()
            }
        }, 0, 600, TimeUnit.MILLISECONDS)
    }
}

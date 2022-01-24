package com.runetopic.xlitekt.game.actor.player

import com.runetopic.xlitekt.game.actor.Actor
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.map.Viewport
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.write.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.write.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.write.RebuildNormalPacket

class Player(
    val client: Client,
    var username: String,
    var displayName: String
) : Actor(Tile(3222, 3222)) {
    var viewport = Viewport(this)

    suspend fun login() {
        this.previousTile = this.tile
        client.writePacket(RebuildNormalPacket(this, true))
        client.writePacket(IfOpenTopPacket(161))
        renderer.appearance(Render.Appearance.Gender.MALE, -1, -1, false)
        client.writePacket(PlayerInfoPacket(this))
        renderer.clearUpdates()
    }
}

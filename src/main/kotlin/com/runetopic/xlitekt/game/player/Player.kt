package com.runetopic.xlitekt.game.player

import com.runetopic.xlitekt.network.client.Client
import com.runetopic.xlitekt.network.packet.write.IfOpenTopPacket
import com.runetopic.xlitekt.network.packet.write.RebuildNormalPacket

class Player(
    val client: Client
) {
    suspend fun login() {
        client.writePacket(RebuildNormalPacket(true))
        client.writePacket(IfOpenTopPacket(161))
    }
}

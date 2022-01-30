package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.NPCList
import com.runetopic.xlitekt.game.actor.PlayerList
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import kotlinx.coroutines.runBlocking

class World {
    val players = PlayerList(MAX_PLAYERS)
    val npcs = NPCList(MAX_NPCs)

    fun process() = runBlocking {
        players.filterNotNull().filter(Player::online).let { players ->
            players.forEach {
                it.forceMove(Render.ForceMovement(Tile(3220, 3220), 0, 512))
                it.client.writePacket(PlayerInfoPacket(it))
            }
            players.forEach { it.client.writePacket(NPCInfoPacket(it)) }
            players.forEach { it.reset() }
        }
    }

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

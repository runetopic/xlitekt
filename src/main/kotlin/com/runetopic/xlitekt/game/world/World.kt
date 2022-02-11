package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.NPCList
import com.runetopic.xlitekt.game.actor.PlayerList
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket

class World {
    val players = PlayerList(MAX_PLAYERS)
    val npcs = NPCList(MAX_NPCs)

    fun process() = players.filterNotNull().filter(Player::online).apply {
        parallelStream().forEach {
            it.client.writePacket(PlayerInfoPacket(it))
            it.client.writePacket(NPCInfoPacket(it))
        }
        parallelStream().forEach(Player::reset)
    }

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

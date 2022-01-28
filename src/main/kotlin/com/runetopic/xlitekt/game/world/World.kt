package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.NpcList
import com.runetopic.xlitekt.game.actor.PlayerList

data class World(
    val players: PlayerList = PlayerList(),
    val npcs: NpcList = NpcList()
) {
    companion object {
        const val MAX_PLAYERS = 2000
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

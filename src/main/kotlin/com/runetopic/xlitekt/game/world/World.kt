package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.NPCList
import com.runetopic.xlitekt.game.actor.PlayerList

class World {
    val players = PlayerList(MAX_PLAYERS)
    val npcs = NPCList(MAX_NPCs)

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

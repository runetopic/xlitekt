package xlitekt.game.world

import xlitekt.game.actor.NPCList
import xlitekt.game.actor.PlayerList

class World {
    val players = PlayerList(MAX_PLAYERS)
    val npcs = NPCList(MAX_NPCs)

    companion object {
        const val MAX_PLAYERS = 2048
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

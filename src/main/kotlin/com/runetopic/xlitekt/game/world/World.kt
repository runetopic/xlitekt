package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.ActorCollection
import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.player.Player

data class World(
    val players: ActorCollection<Player> = ActorCollection(MAX_PLAYERS),
    val npcs: ActorCollection<NPC> = ActorCollection(MAX_NPCs),
) {
    companion object {
        const val MAX_PLAYERS = 2000
        const val MAX_NPCs = Short.MAX_VALUE.toInt()
    }
}

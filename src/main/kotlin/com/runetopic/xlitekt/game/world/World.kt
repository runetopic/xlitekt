package com.runetopic.xlitekt.game.world

import com.runetopic.xlitekt.game.actor.ActorCollection
import com.runetopic.xlitekt.game.actor.player.Player

data class World(
    val players: ActorCollection<Player> = ActorCollection(MAX_PLAYERS),
) {
    companion object {
        const val MAX_PLAYERS = 2000
    }
}

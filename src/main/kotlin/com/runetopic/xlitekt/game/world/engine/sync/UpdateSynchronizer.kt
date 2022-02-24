package com.runetopic.xlitekt.game.world.engine.sync

import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.plugin.koin.inject
import kotlinx.coroutines.Runnable

/**
 * @author Jordan Abraham
 */
class UpdateSynchronizer : Runnable {

    private val world by inject<World>()

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online).toSet()
        players.parallelStream().forEach(Player::updateAppearance)
    }
}

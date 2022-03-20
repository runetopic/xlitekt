package xlitekt.game

import xlitekt.game.loop.Loop
import xlitekt.game.world.World
import xlitekt.shared.inject

class Game {
    private val loop by inject<Loop>()
    private val world by inject<World>()

    fun start() {
        world.build()
        loop.start()
    }

    fun shutdown() {
        loop.shutdown()
    }
}

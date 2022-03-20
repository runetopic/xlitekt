package xlitekt.game

import xlitekt.game.loop.GameLoop
import xlitekt.game.world.World
import xlitekt.shared.inject

class Game {
    private val loop = GameLoop()
    private val world by inject<World>()

    fun start() {
        world.build()
        loop.start()
    }

    fun shutdown() {
        loop.shutdown()
    }
}

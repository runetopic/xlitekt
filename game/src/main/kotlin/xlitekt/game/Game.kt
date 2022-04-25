package xlitekt.game

import xlitekt.game.world.World
import xlitekt.shared.inject

class Game {
    private val gameLoop by inject<GameLoop>()
    private val world by inject<World>()

    fun start() {
        world.build()
        gameLoop.start()
    }

    fun shutdown() {
        gameLoop.shutdown()
    }
}

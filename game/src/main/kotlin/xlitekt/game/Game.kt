package xlitekt.game

import xlitekt.game.world.engine.LoopTask

class Game {
    private val loop = LoopTask()

    fun start() {
        loop.start()
    }

    fun shutdown() {
        loop.shutdown()
    }
}

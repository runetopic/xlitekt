package xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.world.World
import xlitekt.shared.inject

class Game {

    private val logger = InlineLogger()
    private val gameLoop by inject<GameLoop>()
    private val world by inject<World>()

    fun start() {
        world.build()
        gameLoop.start()
    }

    fun shutdown() {
        logger.debug { "Shutting down game loop..." }
        gameLoop.shutdown()
    }
}

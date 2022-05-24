package xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.world.World
import xlitekt.shared.inject

class Game {

    private val logger = InlineLogger()
    private val gameLoop by inject<GameLoop>()
    private val world by inject<World>()
    private val playerJsonEncoderService by inject<PlayerJsonEncoderService>()

    fun start() {
        world.build()
        playerJsonEncoderService.start()
        gameLoop.start()
    }

    fun shutdown() {
        logger.debug { "Shutting down game loop..." }
        gameLoop.shutdown()
        logger.debug { "Shutting down player save encoder service..." }
        playerJsonEncoderService.shutdown()
    }
}

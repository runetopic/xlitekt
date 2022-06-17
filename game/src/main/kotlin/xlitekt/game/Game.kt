package xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import xlitekt.game.fs.PlayerJsonEncoderService
import xlitekt.game.world.World
import xlitekt.shared.inject

/**
 * @author Jordan Abraham
 */
class Game {

    var online = false
    var shuttingdown = false

    private val logger = InlineLogger()
    private val world by inject<World>()
    private val playerJsonEncoderService by inject<PlayerJsonEncoderService>()

    fun start() {
        world.build()
        playerJsonEncoderService.start()
        online = true
    }

    fun shutdown() {
        logger.debug { "Shutting down game service..." }
        online = false
        shuttingdown = true
        logger.debug { "Shutting down player save encoder service..." }
        playerJsonEncoderService.shutdown()
    }
}

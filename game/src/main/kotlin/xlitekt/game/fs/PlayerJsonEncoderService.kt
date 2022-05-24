package xlitekt.game.fs

import com.github.michaelbull.logging.InlineLogger
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream
import xlitekt.game.actor.player.Player
import java.nio.file.Path
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit
import kotlin.io.path.createDirectories
import kotlin.io.path.notExists
import kotlin.io.path.outputStream

/**
 * @author Jordan Abraham
 */
class PlayerJsonEncoderService(
    private val executor: ScheduledExecutorService,
) : Runnable {
    private val logger = InlineLogger()
    private val json = Json { prettyPrint = true }
    private val saveRequests: ConcurrentHashMap.KeySetView<Player, Boolean> = ConcurrentHashMap.newKeySet()

    override fun run() {
        val path = Path.of("./players/").apply {
            if (notExists()) createDirectories()
        }
        saveRequests.onEach {
            try {
                json.encodeToStream(it, Path.of("$path/${it.username}.json").outputStream())
                logger.debug { "Saved player ${it.username} successfully..." }
            } catch (exception: Exception) {
                logger.error(exception) { "Failed to save player ${it.username}..." }
            }
        }.also(saveRequests::removeAll)
    }

    fun requestSave(player: Player) {
        saveRequests += player
    }

    fun start(): ScheduledFuture<*> = executor.scheduleAtFixedRate(this, 600, 600, TimeUnit.MILLISECONDS)
    fun shutdown() = executor.shutdown()
}

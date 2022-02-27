package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.shared.inject
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()
    private val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())
    private var tick = 0

    override fun run() {
        val start = System.nanoTime()
        val players = world.players.filterNotNull().filter(Player::online)

        if (tick % 4 == 0) {
            players.forEach { it.animate(411) }
        }

        val pending = players.associateWith { it.pendingUpdates().toList() }
        val updates = mutableMapOf<Player, ByteReadPacket>()
        val updateLatch = CountDownLatch(players.size)
        players.forEach {
            pool.execute {
                pending[it]?.buildPlayerUpdateBlocks(it)?.run {
                    updates[it] = this
                }
                updateLatch.countDown()
            }
        }
        updateLatch.await(start)
        val locations = players.associateWith(Player::location)
        val syncLatch = CountDownLatch(players.size)
        players.forEach {
            pool.execute {
                val viewport = it.viewport
                it.write(PlayerInfoPacket(viewport, updates, locations))
                it.write(NPCInfoPacket(viewport))
                it.flushPool()
                syncLatch.countDown()
            }
        }
        syncLatch.await(start)
        players.forEach(Player::reset)
        tick++
        logger.debug { "Synchronization took ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)}ms for ${players.size} players." }
    }

    fun shutdown() {
        pool.shutdown()
    }

    private fun CountDownLatch.await(start: Long) = await(600 - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start), TimeUnit.MILLISECONDS)
}
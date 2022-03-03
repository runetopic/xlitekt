package xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import org.rsmod.pathfinder.SmartPathFinder
import xlitekt.game.actor.player.Player
import xlitekt.game.actor.render.block.buildPlayerUpdateBlocks
import xlitekt.game.packet.NPCInfoPacket
import xlitekt.game.packet.PlayerInfoPacket
import xlitekt.game.world.World
import xlitekt.game.world.map.location.Location
import xlitekt.game.world.map.zone.ZoneFlags
import xlitekt.shared.inject
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.random.Random

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

        if (tick % 10 == 0) {
            val pathfinderLatch = CountDownLatch(players.size)
            players.forEach { player ->
                pool.execute {
                    val path = SmartPathFinder(
                        flags = ZoneFlags.flags,
                        defaultFlag = 0
                    ).findPath(
                        srcX = player.location.x,
                        srcY = player.location.z,
                        destX = 3222,
                        destY = 3222,
                        z = player.location.level
                    )
                    player.movement.reset()
                    player.movement.addAll(path.coords.map { Location(it.x, it.y, player.location.level) })
                    pathfinderLatch.countDown()
                }
            }
            pathfinderLatch.await(start)
        }

        val pending = players.associateWith { it.pendingUpdates().toList() }
        val updates = mutableMapOf<Player, ByteReadPacket>()
        val preLatch = CountDownLatch(players.size)
        players.forEach {
            pool.execute {
                it.movement.process()
                val packet = pending[it]?.buildPlayerUpdateBlocks(it)
                if (packet != null) {
                    updates[it] = packet
                }
                preLatch.countDown()
            }
        }
        preLatch.await(start)
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
        logger.debug { "Synchronization took ${TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start)}ms for ${players.size} players." }
        tick++
    }

    fun shutdown() {
        pool.shutdown()
    }

    private fun CountDownLatch.await(start: Long) = await(600 - TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - start), TimeUnit.MILLISECONDS)
}

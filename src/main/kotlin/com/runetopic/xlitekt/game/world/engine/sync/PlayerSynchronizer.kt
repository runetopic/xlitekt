package com.runetopic.xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler.Companion.pendingUpdatesBlocks
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.utils.io.core.ByteReadPacket
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.time.measureTime
import kotlinx.coroutines.Runnable

/**
 * @author Jordan Abraham
 */
class PlayerSynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()
    private val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    override fun run() {
        val players = world.players.filterNotNull().filter(Player::online)

        val time = measureTime {
            val pending = players.associateWith { it.pendingUpdates().toList() }
            val updates = mutableMapOf<Player, ByteReadPacket>()
            val preLatch = CountDownLatch(players.size)
            players.forEach {
                pool.execute {
                    pending[it]?.pendingUpdatesBlocks(it)?.run {
                        updates[it] = this
                    }
                    preLatch.countDown()
                }
            }
            preLatch.await()
            val mainLatch = CountDownLatch(players.size)
            players.forEach {
                pool.execute {
                    it.write(PlayerInfoPacket(it, updates))
                    it.write(NPCInfoPacket(it))
                    it.flushPool()
                    mainLatch.countDown()
                }
            }
            mainLatch.await()
            players.forEach(Player::reset)
        }
        logger.debug { "Synchronization took $time for ${players.size} players." }
    }

    fun shutdown() {
        pool.shutdown()
    }
}

package com.runetopic.xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.network.packet.assembler.PlayerInfoPacketAssembler.Companion.pendingUpdatesBlocks
import com.runetopic.xlitekt.plugin.koin.inject
import io.ktor.utils.io.core.ByteReadPacket
import kotlinx.coroutines.Runnable
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.time.measureTime

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
            val updateLatch = CountDownLatch(players.size)
            players.forEach {
                pool.execute {
                    pending[it]?.pendingUpdatesBlocks(it)?.run {
                        updates[it] = this
                    }
                    updateLatch.countDown()
                }
            }
            updateLatch.await()
            val locations = players.associateWith(Player::location)
            val syncLatch = CountDownLatch(players.size)
            players.forEach {
                pool.execute {
                    it.write(PlayerInfoPacket(it, updates, locations))
                    it.write(NPCInfoPacket(it))
                    it.flushPool()
                    syncLatch.countDown()
                }
            }
            syncLatch.await()
            players.forEach(Player::reset)
        }
        logger.debug { "Synchronization took $time for ${players.size} players." }
    }

    fun shutdown() {
        pool.shutdown()
    }
}

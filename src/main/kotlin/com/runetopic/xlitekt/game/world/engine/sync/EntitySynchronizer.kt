package com.runetopic.xlitekt.game.world.engine.sync

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.actor.player.Player
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NPCInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.plugin.koin.inject
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit
import kotlin.time.measureTime
import kotlinx.coroutines.Runnable

/**
 * @author Jordan Abraham
 */
class EntitySynchronizer : Runnable {

    private val logger = InlineLogger()
    private val world by inject<World>()
    private val pool = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors())

    override fun run() {

        val time = measureTime {
            val players = world.players.filterNotNull().filter(Player::online)
            println(players.size)
            val updates = players.associateWith { it.pendingUpdates().toList() }
            val latch = CountDownLatch(players.size)

            players.forEach {
                pool.execute {
                    it.write(PlayerInfoPacket(it, updates[it] ?: emptyList()))
                    it.write(NPCInfoPacket(it))
                    latch.countDown()
                }
            }
            latch.await(600, TimeUnit.MILLISECONDS)
            players.forEach {
                pool.execute(it::flushPool)
            }
            players.filter{ !it.nextTick }.forEach(Player::reset)
//            players.parallelStream().forEach {
//                it.write(PlayerInfoPacket(it))
//                it.write(NPCInfoPacket(it))
//                it.flushPool()
//            }
//            players.parallelStream().forEach(Player::reset)
        }
        logger.debug { "Synchronization took $time." }
    }
}

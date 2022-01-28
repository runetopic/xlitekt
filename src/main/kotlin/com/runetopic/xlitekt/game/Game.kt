package com.runetopic.xlitekt.game

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.packet.NpcInfoPacket
import com.runetopic.xlitekt.network.packet.PlayerInfoPacket
import com.runetopic.xlitekt.plugin.ktor.inject
import com.runetopic.xlitekt.util.resource.loadAllMapSquares
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import java.util.Timer
import kotlin.concurrent.timerTask
import kotlin.time.measureTime

val gameModule = module {
    single { loadAllMapSquares() }
    single { World() }
    single { Game() }
}

class Game {
    private val logger = InlineLogger()
    private val world = inject<World>()
    private val loop = Timer()

    fun start() {
        loop.scheduleAtFixedRate(
            timerTask {
                val time = measureTime {
                    runBlocking {
                        world.value.players.forEach {
                            it.client.writePacket(PlayerInfoPacket(it))
                        }
                        world.value.players.forEach {
                            it.renderer.clearUpdates()
                        }
                        world.value.players.forEach {
                            it.client.writePacket(NpcInfoPacket(it))
                        }
//                        world.value.players.let { players ->
//                            players.forEach { it.renderer.clearUpdates() }
//                            players.forEach { it.client.writePacket(NpcInfoPacket(it)) }
//                        }
                    }
                }
                logger.debug { "Loop took $time to complete." }
            },
            0L, 600L
        )
    }

    fun shutdownGracefully() {
        logger.debug { "Shutting down game loop..." }
        loop.cancel()
    }
}

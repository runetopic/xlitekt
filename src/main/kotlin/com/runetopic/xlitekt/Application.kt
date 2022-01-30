package com.runetopic.xlitekt

import com.github.michaelbull.logging.InlineLogger
import com.runetopic.xlitekt.game.Game
import com.runetopic.xlitekt.game.actor.npc.NPC
import com.runetopic.xlitekt.game.actor.render.Render
import com.runetopic.xlitekt.game.tile.Tile
import com.runetopic.xlitekt.game.world.World
import com.runetopic.xlitekt.network.Network
import com.runetopic.xlitekt.plugin.ktor.installKoin
import io.ktor.application.Application
import io.ktor.server.engine.commandLineEnvironment
import java.util.TimeZone
import org.koin.core.context.stopKoin
import org.koin.ktor.ext.get

private val logger = InlineLogger()

fun main(args: Array<String>) = commandLineEnvironment(args).start()

fun Application.module() {
    addShutdownHook()
    logger.info { "Starting XliteKt." }
    TimeZone.setDefault(TimeZone.getTimeZone("UTC"))
    installKoin()
    get<Game>().start()
    val npc = NPC(10, Tile(3220, 3220))
    get<World>().npcs.add(npc)
    npc.faceActor(1 + 32768)
    npc.transmog(400)
    npc.forceMove(Render.ForceMovement(Tile(3222, 3222), 0, 512))
    npc.recolor(Render.Recolor(0, 6, 28, 112, 0, 240)) // Nex color
    get<Network>().awaitOnPort(environment.config.property("ktor.deployment.port").getString().toInt())
}

fun Application.addShutdownHook() {
    Runtime.getRuntime().addShutdownHook(
        Thread {
            logger.debug { "Running shutdown hook..." }
            get<Game>().shutdownGracefully()
            get<Network>().shutdownGracefully()
            logger.debug { "Stopping koin..." }
            stopKoin()
        }
    )
}
